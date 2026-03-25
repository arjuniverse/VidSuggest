package com.example.vidsuggest.repository;

import com.example.vidsuggest.entity.Video;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByCategoryIgnoreCase(String category);
    List<Video> findByCategoryIgnoreCase(String category, Sort sort);
}

