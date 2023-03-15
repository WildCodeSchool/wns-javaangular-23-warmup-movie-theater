package fr.cinema.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.cinema.domain.model.Movie;
import fr.cinema.domain.model.MovieCollection;
import fr.cinema.domain.services.MoviesService;
import fr.cinema.repositories.MoviesCollectionRepository;

@RestController
@RequestMapping("/movies")
public class MoviesController{

    @Autowired
    MoviesService moviesService;

    @Autowired
    MoviesCollectionRepository collections;

    @PostMapping("/createCollection")
    public void createCollection(@RequestParam String title) throws Exception {
        collections.save(new MovieCollection(title));
    }

    public static record NewMovieDTO(String title, float price, short year, List<String> times) {
    }

    @PostMapping("/newMovie")
    public void createMovie(@RequestBody NewMovieDTO body) throws Exception {
        moviesService.registerMovie(body.title(), body.price(), body.year(), body.times());
    }

    public static record NewMovieWithoutTimesDTO(String title, float price, short year) {
    }

    @PostMapping("/newMovieWithoutTimes")
    
    public void createMoviePartial(@RequestBody NewMovieWithoutTimesDTO body) throws Exception {
        moviesService.registerMovie(body.title(), body.price(), body.year(), new ArrayList<>());
    }

    public static record UpdateMovieYearDTO(Long id,short year) {
    }

    @PutMapping("/updateYear")
    public void updateMovieYear(@RequestBody UpdateMovieYearDTO body) throws Exception {
        moviesService.updateMovie(body.id(), Optional.empty(), Optional.empty(), Optional.of(body.year()), Optional.empty());
    }

    public static record UpdateMovieDTO(Long id,String title, float price, short year, List<String> times) {
    }

    @PutMapping("/update")
    public void updateMovie(@RequestBody UpdateMovieDTO body) throws Exception {
        moviesService.updateMovie(body.id(), Optional.of(body.title), Optional.of(body.price), Optional.of(body.year()), Optional.of(body.times));
    }

    /**
     * Cette méthode permet de chercher un film par son titre. Il faut que le nom
     * soit exactement le même
     * 
     * @param title le nom du film
     * @return
     * @throws Exception si la base ne peut pas être lue
     */
    @GetMapping("/searchBy")
    public List<Movie> getMovieByTitle(@RequestParam String title) throws Exception {
        List<Movie> movie = moviesService.searchMoviesByTitle(title);
        if (movie.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ce   film n'existe pas !");
        }
        return movie;
    }

    /**
     * Cette méthode permet de chercher un film par son ID
     * 
     * @param id l'id du film
     * @return
     * @throws Exception si la base ne peut pas être lue
     */
    @GetMapping("/{id}")
    public Optional<Movie> getMovieById(@PathVariable Long id) throws Exception {
        return moviesService.getMovieById(id);
    }

    @GetMapping("")
    public List<Movie> getAllMovies() {
        return moviesService.getAllMovies();
    }

}