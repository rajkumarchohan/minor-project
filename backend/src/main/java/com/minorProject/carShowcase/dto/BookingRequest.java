package com.minorProject.carShowcase.dto;

import com.minorProject.carShowcase.model.Showroom;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for incoming booking/pre-booking requests.
 * This structure matches the JSON sent by the frontend form.
 */
@Data
public class BookingRequest {

    @NotNull(message = "Car ID is required")
    private Long carId;

    @NotNull(message = "Showroom ID is required")
    private Long showroomId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String customerEmail;

    // Phone is optional for pre-booking but included in the form
    private String customerPhone;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be in the present or future")
    private LocalDate bookingDate;

}
