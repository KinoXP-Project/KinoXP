package ek.kinoxp.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateBookingRequest {

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @NotBlank(message = "Customer email is required")
        @Email(message = "Invalid email format")
        private String customerEmail;

        @NotNull(message = "Show ID is required")
        private Long showId;

        @NotEmpty(message = "At least one seat must be selected")
        private List<SeatDTO> seats;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public List<SeatDTO> getSeats() { return seats; }
    public void setSeats(List<SeatDTO> seats) { this.seats = seats; }
}
