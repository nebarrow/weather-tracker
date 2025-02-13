package com.nebarrow.weathertracker.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Builder
@Table(name = "Sessions")
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(referencedColumnName = "ID")
    private int userId;

    @Column(name = "ExpiresAt")
    private LocalDateTime expiresAt;
}
