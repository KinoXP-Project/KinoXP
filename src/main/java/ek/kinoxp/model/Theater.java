package ek.kinoxp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "theaters")
public class Theater
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long theaterId;

    private int rowCount;
    private int seatCount;
    private String name;

    public Theater(){}

    public Theater(Long theaterId, int rowCount, int seatCount, String name)
    {
        this.theaterId = theaterId;
        this.rowCount = rowCount;
        this.seatCount = seatCount;
        this.name = name;
    }

    //Getters og setters
    public Long getTheaterId() {return theaterId;}
    public void setTheaterId(Long theaterId) {this.theaterId = theaterId;}

    public int getRowCount() {return rowCount;}
    public void setRowCount(int rowCount) {this.rowCount = rowCount;}

    public int getSeatCount() {return seatCount;}
    public void setSeatCount(int seatCount) {this.seatCount = seatCount;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

}
