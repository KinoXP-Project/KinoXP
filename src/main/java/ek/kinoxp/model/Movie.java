package ek.kinoxp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;
    private String title;
    private String category;
    private int ageLimit;
    private int durationMin;
    private int releaseYear;
    private String actors;
    private String description;
    private String language;


//    @Column(name = "image_url")
//    private String imageUrl;


    // One movie = many shows
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Show> shows = new ArrayList<>();


    public Movie(Long movieId, String title, String category, int ageLimit, int durationMin,
                 int releaseYear, String actors, String description, String language) {} {
        this.movieId = movieId;
        this.title = title;
        this.category = category;
        this.ageLimit = ageLimit;
        this.durationMin = durationMin;
        this.releaseYear = releaseYear;
        this.actors = actors;
        this.description = description;
        this.language = language;


    }

    public Movie() {

    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
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

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;

    }
    }

