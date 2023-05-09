package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = "SELECT * FROM temp_events as te WHERE te.category IN(:categories) or te.category = 0 and te.child = false ORDER BY RAND() LiMIT 1", nativeQuery = true)
    Event findRandomEvent(@Param("categories") Set<Integer> categories);
    @Query(value = "SELECT count(te) = 1 FROM Event as te LEFT JOIN te.answers an WHERE te.id = :eventId AND an.id = :answerId")
    Optional<Boolean> existsEventByIdAndAnswersIn(@Param("eventId") int eventId, @Param("answerId") int answerId);
    //Boolean existsEventByIdAAndAnswersAnd(int event, int answer);
    // Поиск моделей Event с сортировкой и пагинацией
    Page<Event> findAllByOrderByUpdateTimeDesc(Pageable pageable);
}
