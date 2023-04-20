package com.example.recordwebapi.repoitory;

import com.example.recordwebapi.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {
  boolean existsByNameIgnoreCase(String name);

  Album findByName(String name);
}
