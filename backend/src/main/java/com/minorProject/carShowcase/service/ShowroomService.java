package com.minorProject.carShowcase.service;

import com.minorProject.carShowcase.model.Showroom;
import com.minorProject.carShowcase.repository.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowroomService {

    @Autowired
    private ShowroomRepository showroomRepository;

    public List<Showroom> getAllShowrooms() {
        return showroomRepository.findAll();
    }
}
