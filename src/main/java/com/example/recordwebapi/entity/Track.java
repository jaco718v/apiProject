package com.example.recordwebapi.entity;

import com.example.recordwebapi.dto.AlbumTrack;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Track {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String position;

  private String title;

  private String duration;


  public Track(AlbumTrack a) {
    this.position = a.getPosition();
    this.title = a.getTitle();
    this.duration = a.getDuration();
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
