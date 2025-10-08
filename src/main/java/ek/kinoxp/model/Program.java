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
    private Long program_id;

    @OneToMany(mappedBy = "program")
    private List<Show> shows = new ArrayList<>(); //en arrayliste, da der er FLERE shows til ét program

    private LocalDate startDate;

    private LocalDate endDate;

    public Program(){}

    public Program(Long program_id, List<Show> shows, LocalDate startDate, LocalDate endDate)
    {
        this.program_id = program_id;
        this.shows = shows;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //Getters og setters

    public Long getProgram_id() {return program_id;}
    public void setProgram_id(Long  program_id) {this.program_id = program_id;}

    public List<Show> getShows() {return shows;}

    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate() {return endDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

}
