package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM events ORDER BY RAND() LiMIT 1", nativeQuery = true)
    Event findRandomEvent();

    // Поиск моделей Event с сортировкой и пагинацией
    Page<Event> findAllByOrderByUpdateTimeDesc(Pageable pageable);
}
