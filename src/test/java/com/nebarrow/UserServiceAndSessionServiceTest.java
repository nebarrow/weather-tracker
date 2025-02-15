package com.nebarrow;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
import com.nebarrow.weathertracker.model.entity.Session;
import com.nebarrow.weathertracker.repository.SessionRepository;
import com.nebarrow.weathertracker.repository.UserRepository;
import com.nebarrow.weathertracker.service.AuthenticationService;
import com.nebarrow.weathertracker.service.SessionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("application-test")
public class UserServiceAndSessionServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void AuthService_TrySaveNewUser_ShouldSaveUserAtDb() {
        authenticationService.register(new RegistrationRequest("test", "test", "test"));
        var savedUser = userRepository.findByLogin("test");
        assertNotNull(savedUser);
    }

    @Test
    public void AuthService_TrySaveUserWhenHeExists_ShouldThrowException() {
        authenticationService.register(new RegistrationRequest("test", "test", "test"));
        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(new RegistrationRequest("test", "test", "test")));
    }

    @Test
    public void SessionService_CheckCreatingSessionAfterCreatingUser_ShouldCreateSession() {
        authenticationService.register(new RegistrationRequest("test", "test", "test"));
        var sessionId = authenticationService.login(new PostUser("test", "test"));
        var session = sessionService.findById(sessionId);
        assertNotNull(session);
    }

    @Test
    public void SessionService_ShouldExpireSessionAfterTimeout() {
        authenticationService.register(new RegistrationRequest("test", "test", "test"));
        var session = Session.builder()
                .userId(0)
                .expiresAt(LocalDateTime.now().minusSeconds(2))
                .id(UUID.randomUUID())
                .build();
        sessionRepository.save(session);
        assertFalse(sessionService.isActive(session.getId()));
    }
}