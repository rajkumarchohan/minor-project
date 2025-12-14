package com.minorProject.carShowcase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Map;

@Entity
@Table(name = "cars")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   // <-- Add this
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private int year;
    private double price;

    @Column(length = 1024)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showroom_id")
    @JsonIgnore
    private Showroom showroom;

    @ElementCollection
    @CollectionTable(name = "car_specifications", joinColumns = @JoinColumn(name = "car_id"))
    @MapKeyColumn(name = "specifications_key")
    @Column(name = "specifications")
    private Map<String, String> specifications;
    private String ClodinaryId;
}
