package com.example.recordwebapi.dto;

import com.example.recordwebapi.entity.Album;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
public class AlbumResponse {

  private String title;
  private ArrayList<String> genres;

  private ArrayList<String> styles;

  private int releaseYear;

  private ArrayList<AlbumTrack> trackList;

  private ArrayList<String> artists;

  private ArrayList<String> tags;

  public AlbumResponse(AlbumInfo a, ArrayList<String> tags, ArrayList<String> artists) {
    this.title = a.getTitle();
    this.genres = a.getGenres();
    this.styles = a.getStyles();
    this.releaseYear = a.getYear();
    this.trackList = a.getTracklist();
    this.artists = artists;
    this.tags = tags;
  }

  public AlbumResponse(Album a, ArrayList<AlbumTrack> list) {
    this.title = a.getName();
    this.genres = new ArrayList<>(a.getGenres());
    this.styles = new ArrayList<>(a.getStyles());
    this.releaseYear = a.getReleaseYear();
    this.trackList = list;
    this.artists = new ArrayList<>(a.getArtists());
    this.tags = new ArrayList<>(a.getTags());
  }
}
