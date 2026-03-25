package com.example.vidsuggest.controller;

import com.example.vidsuggest.dto.VideoCreateRequest;
import com.example.vidsuggest.dto.VideoRatingUpdateRequest;
import com.example.vidsuggest.dto.VideoResponse;
import com.example.vidsuggest.service.VideoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
@Validated
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<VideoResponse> addVideo(@Valid @RequestBody VideoCreateRequest req) {
        VideoResponse created = videoService.addVideo(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<VideoResponse> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/category/{category}")
    public List<VideoResponse> getVideosByCategory(@PathVariable @NotBlank String category) {
        return videoService.getVideosByCategory(category);
    }

    @GetMapping("/recommend")
    public List<VideoResponse> recommendVideos(
            @RequestParam @NotBlank String category
    ) {
        return videoService.recommendVideosByCategory(category, 10);
    }

    @PatchMapping("/{id}/rating")
    public VideoResponse updateRating(
            @PathVariable Long id,
            @Valid @RequestBody VideoRatingUpdateRequest req
    ) {
        return videoService.updateRating(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}

