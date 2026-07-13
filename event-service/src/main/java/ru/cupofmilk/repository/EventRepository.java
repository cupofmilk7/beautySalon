package ru.cupofmilk.repository;

import ru.cupofmilk.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByFirstDateAfter(LocalDateTime date);

    List<Event> findByFirstDateBetween(LocalDateTime from, LocalDateTime to);

    List<Event> findBySecondDateAfter(LocalDateTime date);

    List<Event> findBySecondDateBetween(LocalDateTime from, LocalDateTime to);

    List<Event> findByMessageDate(LocalDateTime messageDate);

    @Query("SELECT e FROM Event e WHERE e.secondDate >= :now AND e.firstDate <= :now ORDER BY e.secondDate ASC")
    List<Event> findActiveEvents(@Param("now") LocalDateTime now);
}