package com.minorProject.carShowcase.service;

import com.minorProject.carShowcase.dto.BookingRequest;
import com.minorProject.carShowcase.exception.ResourceNotFoundException;
import com.minorProject.carShowcase.model.Booking;
import com.minorProject.carShowcase.model.Car;
import com.minorProject.carShowcase.model.Showroom;
import com.minorProject.carShowcase.repository.BookingRepository;
import com.minorProject.carShowcase.repository.CarRepository;
import com.minorProject.carShowcase.repository.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to handle logic for TEST DRIVE bookings.
 */
@Service
public class BookingService {

    @Autowired
    private ShowroomRepository showroomRepository;

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    public BookingService(BookingRepository bookingRepository, CarRepository carRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
    }

    public Booking createTestDriveBooking(BookingRequest request) {
        // 1. Find the Car
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + request.getCarId()));

        // 2. Find the Showroom (Using the ID sent from the frontend)
        Showroom showroom = showroomRepository.findById(request.getShowroomId()) // Assuming BookingRequest has getShowroomId()
                .orElseThrow(() -> new ResourceNotFoundException("Showroom not found with id: " + request.getShowroomId())); // <--- ADDED

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setShowroom(showroom); // <--- ADDED: Set the Showroom object

        // Set customer and date details
        booking.setUserName(request.getCustomerName());
        booking.setEmail(request.getCustomerEmail());
        booking.setPhone(request.getCustomerPhone());
        booking.setDate(request.getBookingDate().atStartOfDay());

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllTestDriveBookings(){
        List<Booking> all = bookingRepository.findAll();
        return all;
    }

    public List<Booking> getBookingsByShowroomId(Long showroomId) {
        // Simple call to the repository, as filtering is the main task
        return bookingRepository.findByShowroomId(showroomId);
    }
}
