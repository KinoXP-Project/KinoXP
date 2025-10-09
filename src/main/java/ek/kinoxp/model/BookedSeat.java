package ek.kinoxp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "booked_seats",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_show_row_seat",
                columnNames = {"show_id", "row_number", "seat_number"}
        )
)
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booked_seat_id")
    private Long bookedSeatId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    // getters/setters
    public Long getBookedSeatId() { return bookedSeatId; }
    public void setBookedSeatId(Long bookedSeatId) { this.bookedSeatId = bookedSeatId; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}
