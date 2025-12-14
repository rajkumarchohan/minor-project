package com.minorProject.carShowcase.controller;

import com.minorProject.carShowcase.dto.CarDto;
import com.minorProject.carShowcase.model.Car;
import com.minorProject.carShowcase.security.JwtUtil;
import com.minorProject.carShowcase.service.CarService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private CarService carService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{username}")
    public ResponseEntity<?> generateToken(@PathVariable String username){
        return new ResponseEntity<>(jwtUtil.generateToken(username),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCarsForOwner() {
        List<Car> cars = carService.getCarsForCurrentOwner();
        return ResponseEntity.ok(cars);
    }


    @PostMapping()
    public ResponseEntity<?> addCar(
            @RequestPart("carDto") CarDto carDto,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        try {
            // Call your updated service layer method
            Car savedCar = carService.addCarToCurrentOwnerShowroom(carDto, imageFile);

            return new ResponseEntity<>(savedCar, HttpStatus.CREATED);

         } catch (IOException e) {
        System.err.println("File Upload Error: " + e.getMessage()); // Log error on server
        // Return the message to the client for debugging (remove this later)
        return new ResponseEntity<>("Image upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (RuntimeException e) {
            // Handle application errors (e.g., ownerShowroom == null)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) throws Exception {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
