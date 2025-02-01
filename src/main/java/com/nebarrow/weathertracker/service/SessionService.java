package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.model.Session;
import com.nebarrow.weathertracker.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public UUID create(int userId) {
        var session = new Session();
        session.setId(UUID.randomUUID());
        session.setUserId(userId);
        session.setExpiresAt(session.getExpiresAt().plusMinutes(20));
        sessionRepository.save(session);
        return session.getId();
    }
}
