package com.minorProject.carShowcase.repository;

import com.minorProject.carShowcase.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * NEW: Finds all cars associated with a specific showroom ID.
     * This is required by the upgraded CarService.
     */
    List<Car> findAllByShowroomId(Long showroomId);
}