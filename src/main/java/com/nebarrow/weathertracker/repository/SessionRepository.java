package com.nebarrow.weathertracker.repository;

import com.nebarrow.weathertracker.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
}
