package com.minorProject.carShowcase.service;

// CORRECTED IMPORTS to use 'com.minorProject.carshowcase'
import com.minorProject.carShowcase.dto.CarDto;
import com.minorProject.carShowcase.exception.ResourceNotFoundException;
import com.minorProject.carShowcase.model.Car;
import com.minorProject.carShowcase.model.Showroom;
import com.minorProject.carShowcase.model.User;
import com.minorProject.carShowcase.repository.CarRepository;
import com.minorProject.carShowcase.repository.ShowroomRepository;
import com.minorProject.carShowcase.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowroomRepository showroomRepository;
    @Autowired
    private ImageUploadService imageUploadService;

    // --- Public Methods ---

    public List<Car> getCarsByShowroomId(Long showroomId) {
        if (!showroomRepository.existsById(showroomId)) {
            throw new ResourceNotFoundException("Showroom not found with id: " + showroomId);
        }
        return carRepository.findAllByShowroomId(showroomId); // Use the new repository method
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));
    }

    // --- Owner-Specific Methods ---

    @Transactional
    public Car addCarToCurrentOwnerShowroom(CarDto carDto, MultipartFile imageFile) throws IOException {
        // Get the logged-in owner
        User owner = getCurrentUser();
        Showroom ownerShowroom = owner.getShowroom();

        Map uploadedImage = imageUploadService.uploadImage(imageFile);

        if (ownerShowroom == null) {
            throw new RuntimeException("Error: Logged-in owner does not have an associated showroom.");
        }

        // Create new car entity from the DTO
        Car car = new Car();
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setYear(carDto.getYear());
        car.setPrice(carDto.getPrice());
        car.setImageUrl(uploadedImage.get("secure_url").toString());
        car.setClodinaryId(uploadedImage.get("public_id").toString());
        car.setSpecifications(carDto.getSpecifications());

        // Link the car to the owner's showroom
        car.setShowroom(ownerShowroom);

        return carRepository.save(car);
    }

    public List<Car> getCarsForCurrentOwner() {
        User owner = getCurrentUser();
        if (owner.getShowroom() == null) {
            throw new ResourceNotFoundException("This owner has no showroom.");
        }
        Long showroomId = owner.getShowroom().getId();
        return carRepository.findAllByShowroomId(showroomId); // Use the new repository method
    }

    @Transactional
    public void deleteCar(Long carId) throws Exception {
        User owner = getCurrentUser();

        // Add null check for showroom
        if (owner.getShowroom() == null) {
            throw new ResourceNotFoundException("This owner has no showroom.");
        }
        Long showroomId = owner.getShowroom().getId();

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        // Security Check: Ensure the car belongs to the owner who is trying to delete it
        if (car.getShowroom() == null || !car.getShowroom().getId().equals(showroomId)) {
            throw new RuntimeException("Error: You do not have permission to delete this car.");
        }
        imageUploadService.deleteImageByPublicId(car.getClodinaryId());
        carRepository.delete(car);
    }


    /**
     * Helper method to get the currently authenticated user from Spring Security context.
     */
    // com.example.carshowcase.service.CarService.java (around line 107)

    private User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();

        // Check if the SecurityContext is populated
        if (context == null) {
            // This should not happen in an authenticated request, but handles edge cases
            throw new IllegalStateException("SecurityContext is null. Request is outside of a security context.");
        }

        Authentication authentication = context.getAuthentication();

        // Check if the Authentication object is populated (the key fix!)
        if (authentication == null || !authentication.isAuthenticated()) {
            // If unauthenticated, throw AccessDenied or another appropriate exception
            throw new AccessDeniedException("User is not authenticated for this operation.");
        }

        // Now it is safe to call getName()
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}