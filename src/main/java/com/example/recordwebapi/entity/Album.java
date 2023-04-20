package com.example.recordwebapi.entity;

import com.example.recordwebapi.dto.AlbumArtist;
import com.example.recordwebapi.dto.AlbumResponse;
import com.example.recordwebapi.dto.AlbumTrack;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Album {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @ElementCollection
  private List<String> genres;

  @ElementCollection
  private List<String> styles;

  private int releaseYear;

  @ElementCollection
  private List<String> artists;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Track> trackList;

  @ElementCollection
  private List<String> tags;



  public Album(AlbumResponse a, ArrayList<Track> t) {
    this.genres = a.getGenres();
    this.styles = a.getStyles();
    this.artists = a.getArtists();
    this.releaseYear = a.getReleaseYear();
    this.trackList = t;
    this.tags = a.getTags();
    this.name = a.getTitle();
  }
}
