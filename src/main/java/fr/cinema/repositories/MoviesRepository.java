package fr.cinema.repositories;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cinema.domain.events.MovieRegisteredEvent;
import fr.cinema.domain.events.MovieUpdatedEvent;
import fr.cinema.domain.model.Movie;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);

    @EventListener
    @Order(1)
    default void onMovieRegistered(MovieRegisteredEvent event) {
        System.out.println("MOVIE CREATED " + event.movie());
        save(event.movie());
    }

    @EventListener
    @Order(1)
    default void onMovieUpdated(MovieUpdatedEvent event) {
        System.out.println("MOVIE UPDATED " + event.movie());
        save(event.movie());
    }
}
