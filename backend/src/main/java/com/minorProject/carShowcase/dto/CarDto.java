package com.minorProject.carShowcase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * DTO for creating or updating a car.
 * The showroom_id will be added by the service from the logged-in owner.
 */
@Data
public class CarDto {
    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    @Min(1980)
    private int year;

    @NotNull
    @Min(0)
    private double price;

    private String imageUrl;

    // Optional specifications
    private Map<String, String> specifications;
}