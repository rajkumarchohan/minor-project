package com.minorProject.carShowcase.service;

// CORRECTED IMPORTS
import com.minorProject.carShowcase.dto.AuthResponse;
import com.minorProject.carShowcase.dto.LoginRequest;
import com.minorProject.carShowcase.dto.RegisterRequest;
import com.minorProject.carShowcase.model.Showroom;
import com.minorProject.carShowcase.model.User;
import com.minorProject.carShowcase.repository.ShowroomRepository;
import com.minorProject.carShowcase.repository.UserRepository;
import com.minorProject.carShowcase.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowroomRepository showroomRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;



    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Create user
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole() != null ? request.getRole() : "ROLE_USER"
        );

        // Handle showroom
        if ("ROLE_OWNER".equals(request.getRole())) {

            if (request.getShowroomName() == null || request.getShowroomName().isBlank()) {
                throw new RuntimeException("Showroom name is required for owner");
            }

            Showroom showroom = new Showroom(
                    request.getShowroomName(),
                    request.getShowroomLocation()
            );

            showroomRepository.save(showroom);

            user.setShowroom(showroom);
        }

        userRepository.save(user);
    }


    public AuthResponse login(LoginRequest loginRequest) {

        User dbUser = userRepository.findByUsername(loginRequest.getUsername());
        System.out.println("DB PASSWORD = [" + dbUser.getPassword() + "]");
        System.out.println("LENGTH = " + dbUser.getPassword().length());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return new AuthResponse(
                jwt,
                userDetails.getUsername(),
                (String) userDetails.getRole()
        );
    }

    public String findByUsername(String username){
        try {
            return  userRepository.findByUsername(username).getShowroom().getId().toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}