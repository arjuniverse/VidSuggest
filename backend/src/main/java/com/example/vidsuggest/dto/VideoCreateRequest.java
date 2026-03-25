package com.example.vidsuggest.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VideoCreateRequest {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "category is required")
    private String category;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "youtubeLink is required")
    @Pattern(regexp = "^(http|https)://.*", message = "youtubeLink must be a valid http(s) URL")
    private String youtubeLink;

    @NotNull(message = "rating is required")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double rating;

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

