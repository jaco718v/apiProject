package com.example.recordwebapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
public class AlbumSearch {
  private ArrayList<AlbumURL> results;
}
