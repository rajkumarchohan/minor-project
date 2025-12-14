package com.minorProject.carShowcase.controller;

import com.minorProject.carShowcase.dto.AuthResponse;
import com.minorProject.carShowcase.dto.LoginRequest;
import com.minorProject.carShowcase.dto.RegisterRequest;
import com.minorProject.carShowcase.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<?> find(@PathVariable String username) {
        return ResponseEntity.ok(authService.findByUsername(username));
    }
}