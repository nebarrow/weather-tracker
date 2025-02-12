package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.mapper.SessionMapper;
import com.nebarrow.weathertracker.model.entity.Session;
import com.nebarrow.weathertracker.repository.SessionRepository;
import com.nebarrow.weathertracker.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
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

    public void delete(UUID id) {
        sessionRepository.deleteById(id);
    }

    public void createSessionAndAddCookie(UUID sessionId, HttpServletResponse response) {
        var cookie = CookieUtil.set(sessionId);
        response.addCookie(cookie);
    }

    public void deleteSessionCookie(String sessionId, HttpServletResponse response) {
        var cookie = CookieUtil.delete(UUID.fromString(sessionId));
        response.addCookie(cookie);
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void deleteExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
