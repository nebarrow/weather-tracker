package com.nebarrow.weathertracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@Table(name = "Locations")
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "ID")
    private User user;

    @Column(name = "Latitude")
    private double latitude;

    @Column(name = "Longtitude")
    private double longitude;
}
