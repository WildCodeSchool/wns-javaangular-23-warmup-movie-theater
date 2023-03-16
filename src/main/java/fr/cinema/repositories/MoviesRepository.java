package fr.cinema.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cinema.domain.events.MovieRegisteredEvent;
import fr.cinema.domain.events.MovieUpdatedEvent;
import fr.cinema.domain.model.Movie;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long> {
    static Logger log = LoggerFactory.getLogger(MoviesRepository.class);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    @EventListener
    @Order(1)
    default void onMovieRegistered(MovieRegisteredEvent event) {
        log.info("MOVIE CREATED - persist in DB: " + event.movie());
        save(event.movie());
    }

    @EventListener
    @Order(1)
    default void onMovieUpdated(MovieUpdatedEvent event) {
        log.info("MOVIE UPDATED - persist in DB: " + event.movie());
        save(event.movie());
    }
}
