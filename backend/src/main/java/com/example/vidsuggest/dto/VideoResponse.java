package com.example.vidsuggest.dto;

public class VideoResponse {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String youtubeLink;
    private Double rating;

    public VideoResponse() {
    }

    public VideoResponse(Long id, String title, String category, String description, String youtubeLink, Double rating) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.youtubeLink = youtubeLink;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}

