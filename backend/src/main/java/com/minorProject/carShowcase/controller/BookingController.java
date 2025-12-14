package com.minorProject.carShowcase.controller;

import com.minorProject.carShowcase.dto.BookingRequest;
import com.minorProject.carShowcase.model.Booking;
import com.minorProject.carShowcase.model.PaymentStatus;
import com.minorProject.carShowcase.model.PreBooking;
import com.minorProject.carShowcase.repository.BookingRepository;
import com.minorProject.carShowcase.repository.PreBookingRepository;
import com.minorProject.carShowcase.service.BookingService;
import com.minorProject.carShowcase.service.PreBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookingController {

    private final BookingService bookingService;
    private final PreBookingService preBookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PreBookingRepository preBookingRepository;

    public BookingController(BookingService bookingService, PreBookingService preBookingService) {
        this.bookingService = bookingService;
        this.preBookingService = preBookingService;
    }

    /**
     * API Endpoint to book a test drive.
     * POST /book
     */
    @PostMapping("/book")
    public ResponseEntity<Booking> bookTestDrive(@Valid @RequestBody BookingRequest request) {
        Booking newBooking = bookingService.createTestDriveBooking(request);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @PostMapping("/prebook")
    public ResponseEntity<PreBooking> preBookCar(@Valid @RequestBody BookingRequest request) {
        PreBooking newPreBooking = preBookingService.createPreBooking(request);
        return new ResponseEntity<>(newPreBooking, HttpStatus.CREATED);
    }

    @GetMapping("/get/test")
    public ResponseEntity<?> getAllTestDrives(){
        try{
            List<Booking> allTestDriveBookings = bookingService.getAllTestDriveBookings();
            return new ResponseEntity<>(allTestDriveBookings,HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/get/prebook")
    public ResponseEntity<?> getAllPreBooking(){
        try{
            List<PreBooking> allTestDriveBookings = preBookingService.getAllBookings();
            return new ResponseEntity<>(allTestDriveBookings,HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/showroom/{showroomId}")
    public ResponseEntity<List<Booking>> getBookingsByShowroom(@PathVariable Long showroomId) {
        List<Booking> bookings = bookingService.getBookingsByShowroomId(showroomId);

        // If the list is empty, return 200 OK with an empty list, not 404
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/showrooms/{showroomId}")
    public ResponseEntity<List<PreBooking>> getPreBookingsByShowroom(@PathVariable Long showroomId) {
        List<PreBooking> bookings = preBookingService.getPreBookingsByShowroomId(showroomId);
        // If the list is empty, return 200 OK with an empty list, not 404
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/prebooking/{id}")
    public ResponseEntity<?> deletePreBooking(@PathVariable Long id) {
        if (!preBookingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PreBooking not found");
        }

        preBookingRepository.deleteById(id);
        return ResponseEntity.ok("PreBooking deleted successfully");
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        if (!bookingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
        }

        bookingRepository.deleteById(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @GetMapping("/varify/{id}")
    public ResponseEntity<?> varifyPreBooking(@PathVariable Long id) {
        if (!preBookingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PreBooking not found");
        }
        PreBooking oldPrebooking = preBookingRepository.findById(id).orElse(null);
        assert oldPrebooking != null;
        oldPrebooking.setPaymentStatus(PaymentStatus.COMPLETED);
        preBookingRepository.save(oldPrebooking);
        return ResponseEntity.ok("Payment varify successfully");
    }

}
