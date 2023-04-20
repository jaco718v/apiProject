package com.example.recordwebapi.service;

import com.example.recordwebapi.dto.*;
import com.example.recordwebapi.entity.Album;
import com.example.recordwebapi.entity.Track;
import com.example.recordwebapi.repoitory.AlbumRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class AlbumService {

  AlbumRepository albumRepository;
  public AlbumService(AlbumRepository albumRepository){
    this.albumRepository = albumRepository;
  }

  @Value("${app.openai-api-key}")
  private String AI_API_KEY;

  @Value("${app.discogs-api-key}")
  private String ALBUM_API_KEY;

  String OPENAI_URL = "https://api.openai.com/v1/completions";

  String DISCOGS_URL = "https://api.discogs.com/database";

  String FIXED_PROMPT = "Output seperated by ','. Generate 12 tags for the album: ";


  WebClient client = WebClient.create();

  public AlbumResponse createAlbumDB(String albumName){

    if(albumRepository.existsByNameIgnoreCase(albumName)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Album with name already exists");
    }

    /*
    if(albumRepository.existsByNameIgnoreCase(albumName)){
      System.out.println("Tjek");
      Album cachedAlbum = albumRepository.findByName(albumName);
      AlbumResponse response = new AlbumResponse(
          cachedAlbum, convertToAlbumTrack(new ArrayList<>(cachedAlbum.getTrackList())));
      return response;
    }
    */

    String inputPrompt = FIXED_PROMPT + albumName;

    Map<String, Object> body = new HashMap<>();

    body.put("model","text-davinci-003");
    body.put("prompt",inputPrompt);
    body.put("temperature", 0);
    body.put("max_tokens", 50);
    body.put("top_p", 1);
    body.put("frequency_penalty", 0.2);
    body.put("presence_penalty", 0);

    ObjectMapper mapper = new ObjectMapper();
    String json = "";
    try {
      json = mapper.writeValueAsString(body);
    } catch (Exception e) {
      e.printStackTrace();
    }

    String discogSearch = "/search?q="+albumName+"&format=album&per_page=1"+"&token="+ALBUM_API_KEY;

    AlbumSearch albumSearch = client.get()
        .uri(DISCOGS_URL+discogSearch)
        .header("User-Agent","smallApiProject")
        .retrieve()
        .bodyToMono(AlbumSearch.class).block();

    if(albumSearch == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error getting request");
    }

    if(albumSearch.getResults().isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No results found");
    }

    String masterURL = albumSearch.getResults().get(0).getMaster_url();

    Mono<AlbumInfo> albumInfo = client.get()
        .uri(masterURL)
        .header("User-Agent","smallApiProject")
        .retrieve()
        .bodyToMono(AlbumInfo.class);

    Mono<OpenApiResponse> openApiResponse = client.post()
        .uri(OPENAI_URL)
        .header("Authorization", "Bearer " +AI_API_KEY)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(json))
        .retrieve()
        .bodyToMono(OpenApiResponse.class);


    AlbumResponse result = Mono.zip(albumInfo,openApiResponse)
        .map(n -> makeAlbumResponse(n.getT1(),n.getT2())).block();

    if(albumRepository.existsByNameIgnoreCase(result.getTitle())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Album with name already exists");
    }

    Album album = new Album(result,
        makeTrackList(result.getTrackList()));

    albumRepository.save(album);



    return result;
  }

  public AlbumResponse makeAlbumResponse(AlbumInfo albumInfo, OpenApiResponse openApiResponse){
    List<String> tags = Arrays.asList(openApiResponse.choices.get(0).text.substring(2).split(", "));
    ArrayList<String> tagList = new ArrayList<>(tags);
    ArrayList<String> artists = makeArtistsToStringArray(albumInfo.getArtists());
    return new AlbumResponse(albumInfo,tagList,artists);
  }

  public ArrayList<Track> makeTrackList(ArrayList<AlbumTrack> list){
    ArrayList<Track> trackArrayList = new ArrayList<>();
    for(AlbumTrack a : list){
      trackArrayList.add(new Track(a));
    }
    return trackArrayList;
  }

  public ArrayList<String> makeArtistsToStringArray(ArrayList<AlbumArtist> list){
    ArrayList<String> stringList = new ArrayList<>();
    for(AlbumArtist a : list){
      stringList.add(a.getName());
    }
    return  stringList;
  }

  public ArrayList<AlbumTrack> convertToAlbumTrack(ArrayList<Track> list){
    ArrayList<AlbumTrack> albumTrackList = new ArrayList<>();
    for(Track t : list){
      albumTrackList.add(new AlbumTrack(t));
    }
    return albumTrackList;
  }
}
