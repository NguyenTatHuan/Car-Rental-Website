package com.springprojects.entity;

import com.springprojects.enums.CarStatus;
import com.springprojects.enums.FuelType;
import com.springprojects.enums.TransmissionType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "UUID", insertable = false, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private CarType carType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransmissionType transmissionType;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private double pricePerDay;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Maintenance> maintenances;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Insurance> insurances;

}
