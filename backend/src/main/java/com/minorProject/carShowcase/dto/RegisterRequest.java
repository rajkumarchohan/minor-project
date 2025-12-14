package com.minorProject.carShowcase.dto;

import lombok.Data;



@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    // Store only ONE role (USER or OWNER)
    private String role;

    // Optional – Only for owners
    private String showroomName;
    private String showroomLocation;
}
