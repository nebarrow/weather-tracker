package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsernameController {
    @Autowired
    private UserService userService;

    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestBody String username) {
        boolean exists = userService.findByLogin(username).isPresent();
        return ResponseEntity.ok("{\"exists\": " + exists + "}");
    }
}
