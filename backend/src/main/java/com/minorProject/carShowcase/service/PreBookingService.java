package com.minorProject.carShowcase.service;

import com.minorProject.carShowcase.dto.BookingRequest;
import com.minorProject.carShowcase.exception.ResourceNotFoundException;
import com.minorProject.carShowcase.model.*;
import com.minorProject.carShowcase.repository.CarRepository;
import com.minorProject.carShowcase.repository.PreBookingRepository;
import com.minorProject.carShowcase.repository.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.minorProject.carShowcase.model.Showroom;

/**
 * Service to handle logic for PRE-BOOKING orders.
 */
@Service
public class PreBookingService {

    private final PreBookingRepository preBookingRepository;
    @Autowired
    private final CarRepository carRepository;
    @Autowired
    private  ShowroomRepository showroomRepository;

    public PreBookingService(PreBookingRepository preBookingRepository, CarRepository carRepository) {
        this.preBookingRepository = preBookingRepository;
        this.carRepository = carRepository;
    }

    public PreBooking createPreBooking(BookingRequest request) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + request.getCarId()));

        Showroom showroom= showroomRepository.findById(request.getShowroomId()).orElseThrow(()->new ResourceNotFoundException("Car not found with id: " + request.getShowroomId()));

        PreBooking preBooking = new PreBooking();
        preBooking.setCar(car);
        preBooking.setUserName(request.getCustomerName());
        preBooking.setEmail(request.getCustomerEmail());
        
        // 'date' for a pre-booking is the time it was created
        preBooking.setDate(LocalDateTime.now());
        // All new pre-bookings start as PENDING
        preBooking.setPaymentStatus(PaymentStatus.PENDING);
        preBooking.setShowroom(showroom);
       preBooking.setPhone(request.getCustomerPhone());
        // Note: We ignore the phone and bookingDate from the request as they aren't in the pre_bookings table.

        return preBookingRepository.save(preBooking);
    }

    public List<PreBooking> getAllBookings(){
        List<PreBooking> all = preBookingRepository.findAll();
        return all;
    }

    public List<PreBooking> getPreBookingsByShowroomId(Long showroomId) {
        // Simple call to the repository, as filtering is the main task
        return preBookingRepository.findByShowroomId(showroomId);
    }
}
