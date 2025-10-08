package ek.kinoxp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programs")
public class Program
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> shows = new ArrayList<>();

    private LocalDate startDate;

    private LocalDate endDate;

    public Program(){}

    public Program(Long programId, LocalDate startDate, LocalDate endDate)
    {
        this.programId = programId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //Getters og setters

    public Long getProgramId() {return programId;}
    public void setProgramId(Long programId) {this.programId = programId;}

    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate() {return endDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

    public List<Show> getShows() {return shows;}

    public void addShow(Show show) {
        shows.add(show);
        show.setProgram(this);
    }

    public void removeShow(Show show) {
        shows.remove(show);
        show.setProgram(null);
    }

}
