package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.mapper.SessionMapper;
import com.nebarrow.weathertracker.model.Session;
import com.nebarrow.weathertracker.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionRepository sessionRepository;

    public UUID create(int userId) {
        var session = new Session();
        session.setId(UUID.randomUUID());
        session.setUserId(userId);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        sessionRepository.save(session);
        return session.getId();
    }

    public Optional<SessionResponse> findById(UUID id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::toDto);
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void deleteExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
