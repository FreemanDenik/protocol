package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM events ORDER BY RAND() LiMIT 1", nativeQuery = true)
    public Event findRandomEvent();
}
