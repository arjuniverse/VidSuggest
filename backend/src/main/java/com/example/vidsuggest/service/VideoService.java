package com.example.vidsuggest.service;

import com.example.vidsuggest.dto.VideoCreateRequest;
import com.example.vidsuggest.dto.VideoRatingUpdateRequest;
import com.example.vidsuggest.dto.VideoResponse;
import com.example.vidsuggest.entity.Video;
import com.example.vidsuggest.exception.ResourceNotFoundException;
import com.example.vidsuggest.repository.VideoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public VideoResponse addVideo(VideoCreateRequest req) {
        Video video = new Video(
                req.getTitle(),
                req.getCategory(),
                req.getDescription(),
                req.getYoutubeLink(),
                req.getRating()
        );
        Video saved = videoRepository.save(video);
        return toResponse(saved);
    }

    public List<VideoResponse> getAllVideos() {
        return videoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<VideoResponse> getVideosByCategory(String category) {
        String normalized = category.trim();
        List<Video> videos = videoRepository.findByCategoryIgnoreCase(normalized);
        return videos.stream()
                .sorted(Comparator.comparing(Video::getRating, Comparator.nullsLast(Double::compareTo)).reversed())
                .map(this::toResponse)
                .toList();
    }

    public List<VideoResponse> recommendVideosByCategory(String category, int limit) {
        String normalized = category.trim();

        // Top-rated logic: sort rating desc, then take the first N
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        List<Video> videos = videoRepository.findByCategoryIgnoreCase(normalized, sort);
        return videos.stream()
                .limit(limit)
                .map(this::toResponse)
                .toList();
    }

    public VideoResponse updateRating(Long id, VideoRatingUpdateRequest req) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
        video.setRating(req.getRating());
        Video saved = videoRepository.save(video);
        return toResponse(saved);
    }

    public void deleteVideo(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Video not found with id: " + id);
        }
        videoRepository.deleteById(id);
    }

    private VideoResponse toResponse(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getCategory(),
                video.getDescription(),
                video.getYoutubeLink(),
                video.getRating()
        );
    }
}

