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
    //@Query(value = "SELECT * FROM temp_events as te WHERE te.category IN(:categories) or te.category = 0 and te.child = false and te.shadow = false ORDER BY RAND() LiMIT 1", nativeQuery = true)
    //Event findRandomEvent(@Param("categories") Set<Integer> categories);

    /**
     * Нативный метод генерации случайного события с учететом приобретенных категории и отдельных (скрытых) событий
     * а так же исключает события которые записаны как одноразовые
     *
     * @param userId id пользователя
     * @param eventId id предыдушего события которое мы исключаем, чтобы не было выпадений подряд
     * @return {@link Event}
     */
//    @Query(value = "SELECT * FROM temp_events AS te WHERE te.id != :eventId AND te.publication = true AND" +
//            " te.id NOT IN(SELECT once_events FROM player_once_events WHERE user_id = :userId) AND" +
//            " (te.category IN(select add_categories FROM player_add_categories WHERE user_id = :userId) OR te.category = 0) AND" +
//            " (te.shadow = false OR te.id IN(SELECT add_events FROM player_add_events WHERE user_id = :userId))" +
//            " ORDER BY RAND() LiMIT 1", nativeQuery = true)
    @Query(value = "SELECT te.* FROM protocol_db.temp_events as te" +
            " LEFT JOIN player_add_events as pae ON pae.add_events = te.id" +
            " LEFT JOIN player_add_categories as pac ON pac.add_categories = te.category" +
            " LEFT JOIN player_once_events  as poe ON poe.once_events = te.id" +
            " WHERE te.id != :eventId AND te.publication = true" +
            " AND ((pae.user_id = :userId AND pae.add_events = te.id) OR te.shadow = false)" +
            " AND ((pac.user_id = :userId AND te.category IN(pac.add_categories)) OR te.category = 0)" +
            " AND ((poe.user_id = :userId AND te.id NOT IN(poe.once_events)) OR poe.once_events IS NULL)" +
            " ORDER BY RAND() LiMIT 1", nativeQuery = true)
    Event findRandomEvent(@Param("userId") int userId, @Param("eventId") int eventId);
    @Query(value = "SELECT count(te) = 1 FROM Event as te LEFT JOIN te.answers an WHERE te.publication = true AND te.id = :eventId AND an.id = :answerId")
    Optional<Boolean> existsEventByIdAndAnswersIn(@Param("eventId") int eventId, @Param("answerId") int answerId);
    //Boolean existsEventByIdAAndAnswersAnd(int event, int answer);
    // Поиск моделей Event с сортировкой и пагинацией
    Page<Event> findAllByOrderByUpdateTimeDesc(Pageable pageable);
}
