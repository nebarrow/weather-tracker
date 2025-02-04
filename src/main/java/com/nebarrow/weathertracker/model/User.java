package com.nebarrow.weathertracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
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

    @Length(min = 5, message = "The password must be at least 5 characters long")
    @Column(name = "Password")
    private String password;
}
