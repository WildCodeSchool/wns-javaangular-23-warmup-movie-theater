package fr.cinema.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cinema.domain.model.MovieCollection;

@Repository
public interface MoviesCollectionRepository extends JpaRepository<MovieCollection, Long> {

  List<MovieCollection> findByTitleContainingIgnoreCase(String title);

  List<MovieCollection> findByMoviesTitleContainingIgnoreCase(String title);

}