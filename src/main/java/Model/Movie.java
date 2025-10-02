package Model;

import jakarta.persistence.*;

@Entity // klassen til en tabel i DB
@Table(name = "movies") //giver tabellen navnet "Movies" i DB
public class Movie
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movie_id;

    @Column(nullable = false) //kan ikke være tom i DB
    private String title;
    private String category;
    private int run_time;
    private int age_limit;
    private int release_year;
    private String language;

    public Movie(){}

    public Movie(int movie_id, String title, String category, int run_time, int age_limit, int release_year, String language)
    {
        this.movie_id = movie_id;
        this.title = title;
        this.category = category;
        this.run_time = run_time;
        this.age_limit = age_limit;
        this.release_year = release_year;
        this.language = language;
    }

    // getters og setters
    public int getMovie_id() { return movie_id; }
    public void setMovie_id(int movie_id) { this.movie_id = movie_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getRun_time() { return run_time; }
    public void setRun_time(int run_time) { this.run_time = run_time; }

    public int getAge_limit() { return age_limit; }
    public void setAge_limit(int age_limit) { this.age_limit = age_limit; }

    public int getRelease_year() { return release_year; }
    public void setRelease_year(int release_year) { this.release_year = release_year; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }


}
