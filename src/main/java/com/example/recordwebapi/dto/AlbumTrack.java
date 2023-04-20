package com.example.recordwebapi.dto;

import com.example.recordwebapi.entity.Album;
import com.example.recordwebapi.entity.Track;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AlbumTrack {
  private String position;

  private String title;

  private String duration;

  public AlbumTrack(Track t) {
    this.position = t.getPosition();
    this.title = t.getTitle();
    this.duration = t.getDuration();
  }
}
