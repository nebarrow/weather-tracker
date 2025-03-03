package com.nebarrow.weathertracker.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "Locations")
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // TODO: нет ограничения не уникальность
    @Column(name = "Name")
    private String name;

    @Column(name = "UserId")
    @JoinColumn(referencedColumnName = "ID")
    private int userId;

    @Column(name = "Latitude")
    private double latitude;

    @Column(name = "Longtitude")
    private double longitude;
}
