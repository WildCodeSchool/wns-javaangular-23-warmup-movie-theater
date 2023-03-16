package fr.cinema.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cinema.domain.events.MovieCollectionCreatedEvent;
import fr.cinema.domain.model.MovieCollection;

@Repository
public interface MoviesCollectionRepository extends JpaRepository<MovieCollection, Long> {
  static Logger log = LoggerFactory.getLogger(MoviesCollectionRepository.class);

  List<MovieCollection> findByTitleContainingIgnoreCase(String title);

  List<MovieCollection> findByMoviesTitleContainingIgnoreCase(String title);

  @EventListener
  @Order(1)
  default void onMovieRegistered(MovieCollectionCreatedEvent event) {
      log.info("MOVIE COLLECTION CREATED - persist in DB: " + event.collection());
      save(event.collection());
  }

}