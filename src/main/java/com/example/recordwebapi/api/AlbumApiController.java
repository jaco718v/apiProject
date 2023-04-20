package com.example.recordwebapi.api;

import com.example.recordwebapi.dto.AlbumResponse;
import com.example.recordwebapi.repoitory.AlbumRepository;
import com.example.recordwebapi.service.AlbumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/album")
public class AlbumApiController {

  AlbumService service;

  public AlbumApiController(AlbumService service){
    this.service=service;
  }

  @GetMapping("/create")
  public AlbumResponse createAlbum(@RequestParam String albumName){
    return service.createAlbumDB(albumName);
  }
}
