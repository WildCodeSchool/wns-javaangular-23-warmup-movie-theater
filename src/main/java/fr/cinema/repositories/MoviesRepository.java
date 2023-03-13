package fr.cinema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cinema.Movie;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long> {
    
}
