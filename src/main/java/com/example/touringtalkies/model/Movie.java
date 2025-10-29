package com.example.touringtalkies.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private int releaseYear;
    private String cast;
    private String crew;
    private String filename;   // video filename
    private String thumbnail;  // thumbnail filename
    private boolean freeContent;
    private double likePercentage;

    // Transient fields for file uploads
    @Transient
    private MultipartFile videoFile;

    @Transient
    private MultipartFile thumbnailFile;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchlistItem> watchlistItems;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieLike> likes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;
    public void recomputeLikePercentage(long totalUsers, long likes) {
        this.likePercentage = totalUsers == 0 ? 0 : (likes * 100.0) / totalUsers;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isFreeContent() {
        return freeContent;
    }

    public void setFreeContent(boolean freeContent) {
        this.freeContent = freeContent;
    }

    public double getLikePercentage() {
        return likePercentage;
    }

    public void setLikePercentage(double likePercentage) {
        this.likePercentage = likePercentage;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }

    public MultipartFile getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(MultipartFile thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }
}
