package fr.cinema.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.cinema.Movie;
import fr.cinema.MoviesDatabase;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    MoviesDatabase moviesDatabase;
    
    /**
     * Cette méthode permet de chercher un film par son. Il faut que le nom soit exactement le même 
     * @param name le nom du film
     * @return
     * @throws Exception si la base ne peut pas être lue
     */
    @GetMapping("/searchByName")
    public Movie getMovieByName(@RequestParam String name) throws Exception {
        Movie movie = moviesDatabase.getMovieInfo(name);
        if (movie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ce   film n'existe pas !");
        }
        return movie;
    }

    @GetMapping("")
    List<Movie> getAllMovies() {
        return moviesDatabase.getAllMovies();
    }

}
