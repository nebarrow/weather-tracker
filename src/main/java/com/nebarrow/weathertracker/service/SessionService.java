package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.mapper.SessionMapper;
import com.nebarrow.weathertracker.model.entity.Session;
import com.nebarrow.weathertracker.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;

    public UUID create(int userId) {
        var session = Session.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        sessionRepository.save(session);
        return session.getId();
    }

    @Transactional
    public SessionResponse findById(UUID id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public void delete(UUID id) {
        sessionRepository.deleteById(id);
    }

    public boolean isActive(UUID sessionId) {
        var session = sessionRepository.findById(sessionId);
        return session.map(s -> !s.isExpired())
                .orElse(false);
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void deleteExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
