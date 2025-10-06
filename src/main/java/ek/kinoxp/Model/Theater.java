package ek.kinoxp.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "theaters")
public class Theater
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theater_id;

    private int row_count;
    private int seat_count;
    private String name;

    public Theater(){}

    public Theater(Long theater_id, int row_count, int seat_count, String name)
    {
        this.theater_id = theater_id;
        this.row_count = row_count;
        this.seat_count = seat_count;
        this.name = name;
    }

    //Getters og setters
    public Long getTheater_id() {return theater_id;}
    public void setTheater_id(Long theater_id) {this.theater_id = theater_id;}

    public int getRow_count() {return row_count;}
    public void setRow_count(int row_count) {this.row_count = row_count;}

    public int getSeat_count() {return seat_count;}
    public void setSeat_count(int seat_count) {this.seat_count = seat_count;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

}
