package com.nebarrow.weathertracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Component
@Table(name = "Sessions")
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID")
    private User user;

    @Column(name = "ExpiresAt")
    private LocalDateTime expiresAt;
}
