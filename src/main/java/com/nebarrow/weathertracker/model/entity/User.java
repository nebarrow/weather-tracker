package com.nebarrow.weathertracker.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email(message = "The entered value does not match the email format")
    @Column(name = "Login", unique = true)
    private String login;

    @Column(name = "Password")
    private String password;
}
