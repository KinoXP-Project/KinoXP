package ek.kinoxp.DTO;

public class SeatDTO {
    private int row;
    private int seat;

    public SeatDTO() {}
    public SeatDTO(int row, int seat) { this.row = row; this.seat = seat; }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getSeat() { return seat; }
    public void setSeat(int seat) { this.seat = seat; }
}
