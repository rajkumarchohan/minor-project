package com.minorProject.carShowcase.repository;

import com.minorProject.carShowcase.model.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowroomRepository extends JpaRepository<Showroom, Long> {
}
