package com.minorProject.carShowcase.repository;

import com.minorProject.carShowcase.model.Booking;
import com.minorProject.carShowcase.model.PreBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreBookingRepository extends JpaRepository<PreBooking, Long> {
    List<PreBooking> findByShowroomId(Long showroomId);
}
