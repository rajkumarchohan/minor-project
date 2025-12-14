package com.minorProject.carShowcase.controller;

import com.minorProject.carShowcase.model.Showroom;
import com.minorProject.carShowcase.repository.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ShowroomController {

    @Autowired
    private ShowroomRepository showroomRepository;

    @GetMapping("/show/all")
    public ResponseEntity<List<Showroom>> getAllShowrooms() {
        try{
            List<Showroom> allShowrooms = showroomRepository.findAll();

            return ResponseEntity.ok(allShowrooms.stream().distinct().collect(Collectors.toList()));
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
