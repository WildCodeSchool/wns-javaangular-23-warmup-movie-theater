package fr.cinema.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.cinema.Movie;
import fr.cinema.MoviesDatabase;
import fr.cinema.repositories.MoviesRepository;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    MoviesDatabase moviesDatabase;
    
    @Autowired
    MoviesRepository moviesRepository;

    /**
     * Cette méthode permet de chercher un film par son titre. Il faut que le nom soit exactement le même 
     * @param title le nom du film
     * @return
     * @throws Exception si la base ne peut pas être lue
     */
    @GetMapping("/searchBy")
    public List<Movie> getMovieByTitle(@RequestParam String title) throws Exception {
        List<Movie> movie = moviesRepository.findByTitleContainingIgnoreCase(title);
        if (movie.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ce   film n'existe pas !");
        }
        return movie;
    }
    /**
     * Cette méthode permet de chercher un film par son ID 
     * @param id l'id  du film
     * @return
     * @throws Exception si la base ne peut pas être lue
     */
    @GetMapping("/{id}")
    public Optional<Movie> getMovieById(@PathVariable Long id) throws Exception {
        return moviesRepository.findById(id);
    }

    @GetMapping("")
    List<Movie> getAllMovies() {
        return moviesDatabase.getAllMovies();
    }

}
