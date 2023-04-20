package com.example.recordwebapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
public class AlbumInfo {

  private ArrayList<String> genres;

  private ArrayList<String> styles;

  private int year;

  private ArrayList<AlbumTrack> tracklist;

  private ArrayList<AlbumArtist> artists;

  private String title;
}
