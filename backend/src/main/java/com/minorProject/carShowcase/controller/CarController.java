package com.minorProject.carShowcase.controller;

import com.minorProject.carShowcase.model.Car;
import com.minorProject.carShowcase.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;


    @GetMapping("/showroom/{showroomId}")
    public ResponseEntity<List<Car>> getCarsByShowroom(@PathVariable Long showroomId) {
        List<Car> cars = carService.getCarsByShowroomId(showroomId);
        return ResponseEntity.ok(cars);
    }


    @GetMapping("/public/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

}