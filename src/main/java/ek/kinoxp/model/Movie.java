package ek.kinoxp.model;

import jakarta.persistence.*;

@Entity // klassen til en tabel i DB
@Table(name = "movies") //giver tabellen navnet "Movies" i DB
public class Movie
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(nullable = false) //kan ikke være tom i DB
    private String title;
    private String category;
    private int runTime;
    private int ageLimit;
    private int releaseYear;
    private String language;

    public Movie(){}

    public Movie(Long movieId, String title, String category, int runTime, int ageLimit, int releaseYear, String language)
    {
        this.movieId = movieId;
        this.title = title;
        this.category = category;
        this.runTime = runTime;
        this.ageLimit = ageLimit;
        this.releaseYear = releaseYear;
        this.language = language;
    }

    // getters og setters
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getRunTime() { return runTime; }
    public void setRunTime(int runTime) { this.runTime = runTime; }

    public int getAgeLimit() { return ageLimit; }
    public void setAgeLimit(int ageLimit) { this.ageLimit = ageLimit; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }


}
