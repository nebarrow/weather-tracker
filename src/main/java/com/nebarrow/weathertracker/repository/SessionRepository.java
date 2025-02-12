package com.nebarrow.weathertracker.repository;

import com.nebarrow.weathertracker.model.entity.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    @Modifying
    @Transactional
    @Query("delete from Session s where s.expiresAt < current timestamp")
    void deleteExpiredSessions();
}
