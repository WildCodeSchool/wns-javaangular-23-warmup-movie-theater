package fr.cinema.controllers;

import java.time.*;
import java.time.temporal.*;
import java.time.chrono.*;
import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.cinema.Movie;
import fr.cinema.MoviesDatabase;
import fr.cinema.repositories.MoviesRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Entity
class MovieCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String title;

    @OneToMany
    List<Movie> movies = new ArrayList<>();

    public MovieCollection(String title) {
        this.title = title;
    }

    public MovieCollection() {
    }


}

@Repository
      interface MoviesCollectionAccess extends JpaRepository<MovieCollection, Long> {

        List<MovieCollection> findByTitleContainingIgnoreCase(String title);
        List<MovieCollection> findByMoviesTitleContainingIgnoreCase(String title);

}


@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    MoviesDatabase moviesDatabase;

    @Autowired
    MoviesRepository moviesRepository;

    @Autowired
    MoviesCollectionAccess collections;

    @PostMapping("/createCollection")
    
    public void createCollection(@RequestParam String title) throws Exception {
        collections.save(new MovieCollection(title));
    }

    public static record NewMovieDTO(String title, float price, short year, List<String> times) {
    }

    @PostMapping("/newMovie")
    
    public void createMovie(@RequestBody NewMovieDTO body) throws Exception {

        var movie = new Movie();

        movie.setPrice(body.price());
        movie.setTitle(body.title());
        movie.setYears(List.of(body.year()));
        movie.setTimes(body.times());

        moviesRepository.save(movie);

        // envoyer une notification mail lorsque certains films intègrent la base de données, le movie theater attendait des films de cette époque avec impatience et tenait donc à être informé par mail
        if (body.year() >= 1980 && body.year() < 1990) {
            envoyerEmail("nouveau film des années 80",
                    "Le film " + body.title() + "(" + body.year() + ")" + " a été intégré");
        } else if (body.year() == 1977) {
            envoyerEmail("un film de 1977 est rentré",
                    "Le film " + body.title() + " de l'année 1977 suivie par mail a été intégré");
        }

        // ajouter le film à une collection
        if (body.year() < 1950) {
            System.out.println("un vieux film est disponible");

            List<MovieCollection>collectionsVieuxFilm = collections.findByTitleContainingIgnoreCase("vieux films");
            
            MovieCollection collectionVieuxFilm;
            if (collectionsVieuxFilm.isEmpty()) {
                collectionVieuxFilm = new MovieCollection("vieux films");
            } else {
                collectionVieuxFilm = collectionsVieuxFilm.get(0);
                
            }
            collectionVieuxFilm.movies.add(movie);
                collections.save(collectionVieuxFilm);
            
        } else if (body.year() == LocalDate.now().getYear()) {
            System.out.println("un film de l'année est disponible");


            List<MovieCollection>collectionsFilmDeLannee = collections.findByTitleContainingIgnoreCase("film de l'année");
            
            MovieCollection targetCollection;
            if (collectionsFilmDeLannee.isEmpty()) {
                targetCollection = new MovieCollection("film de l'année");
            } else {
                targetCollection = collectionsFilmDeLannee.get(0);
                
            }
            targetCollection.movies.add(movie);
                collections.save(targetCollection);
        }
        
    }


    public static record NewMovieWithoutTimesDTO(String title, float price, short year) {
    }

    @PostMapping("/newMovieWithoutTimes")
    
    public void createMoviePartial(@RequestBody NewMovieWithoutTimesDTO body) throws Exception {

        var movie = new Movie();

        movie.setPrice(body.price());
        movie.setTitle(body.title());
        movie.setYears(List.of(body.year()));

        moviesRepository.save(movie);

        // envoyer une notification mail lorsque certains films intègrent la base de données, le movie theater attendait des films de cette époque avec impatience et tenait donc à être informé par mail
        if (body.year() >= 1980 && body.year() < 1990) {
            envoyerEmail("film des years 80",
                    "Le film " + body.title() + "(" + body.year() + ")" + " a été intégré");
        } else if (body.year() == 1977) {
            envoyerEmail("un film de 1977 est rentré",
                    "Le film " + body.title() + " de l'année 1977 is inserted");
        }

        // ajouter le film à une collection
        if (body.year() < 1950) {
            System.out.println("un vieux film est disponible");

            List<MovieCollection>collectionsVieuxFilm = collections.findByTitleContainingIgnoreCase("vieux films");
            
            MovieCollection collectionVieuxFilm;
            if (collectionsVieuxFilm.isEmpty()) {
                collectionVieuxFilm = new MovieCollection("vieux films");
            } else {
                collectionVieuxFilm = collectionsVieuxFilm.get(0);
                
            }
            collectionVieuxFilm.movies.add(movie);
                collections.save(collectionVieuxFilm);
            
        } else if (body.year() == LocalDate.now().getYear()) {
            System.out.println("un film de l'année est disponible");


            List<MovieCollection>collectionsFilmDeLannee = collections.findByTitleContainingIgnoreCase("film de l'année");
            
            MovieCollection targetCollection;
            if (collectionsFilmDeLannee.isEmpty()) {
                targetCollection = new MovieCollection("film de l'année");
            } else {
                targetCollection = collectionsFilmDeLannee.get(0);
                
            }
            targetCollection.movies.add(movie);
                collections.save(targetCollection);
        }
        
    }

    public static record UpdateMovieYearDTO(Long id,short year) {
    }

    @PutMapping("/updateYear")
    public void updateMovieYear(@RequestBody UpdateMovieYearDTO body) throws Exception {

        var movie = moviesRepository.findById(body.id()).get();
System.out.println("movie=" + movie);
        movie.getYears().clear();
        movie.getYears().addAll(List.of(body.year()));
        moviesRepository.save(movie);

        
        // envoyer une notification mail lorsque certains films intègrent la base de données, le movie theater attendait des films de cette époque avec impatience et tenait donc à être informé par mail
        if (body.year() >= 1980 && body.year() < 1990) {
            envoyerEmail("nouveau film des années 80",
                    "Le film " + movie.getTitle() + "(" + body.year() + ")" + " a été intégré");
        } else if (body.year() == 1977) {
            envoyerEmail("un film de 1977 est rentré",
                    "Le film " + movie.getTitle() + " de l'année 1977 suivie par mail a été intégré");
        }

        // supprime le film des collections dans lesquelles il était
        var matchingCollections = collections .findByMoviesTitleContainingIgnoreCase(movie.getTitle());
        for (MovieCollection c : matchingCollections) {
            c.movies.remove(movie);
            collections.save(c);
        }
        
        // ajouter le film à une collection
        String vieuxFilmCOllectionName  = "vieux films";
        if (body.year() < 1950) {
            System.out.println("un vieux film est disponible");

            List<MovieCollection>collectionsVieuxFilm = collections.findByTitleContainingIgnoreCase(vieuxFilmCOllectionName);
            
            MovieCollection collectionVieuxFilm;
            if (collectionsVieuxFilm.isEmpty()) {
                collectionVieuxFilm = new MovieCollection(vieuxFilmCOllectionName);
            } else {
                collectionVieuxFilm = collectionsVieuxFilm.get(0);
                
            }
            collectionVieuxFilm.movies.add(movie);
                collections.save(collectionVieuxFilm);
            
        } else if (body.year() == LocalDate.now().getYear()) {
            System.out.println("un film de l'année est disponible");


            List<MovieCollection>collectionsFilmDeLannee = collections.findByTitleContainingIgnoreCase("film de l'année");
            
            MovieCollection targetCollection;
            if (collectionsFilmDeLannee.isEmpty()) {
                targetCollection = new MovieCollection("film de l'année");
            } else {
                targetCollection = collectionsFilmDeLannee.get(0);
                
            }
            targetCollection.movies.add(movie);
                collections.save(targetCollection);
        }
        
    }

    public static record UpdateMovieDTO(Long id,String title, float price, short year, List<String> times) {
    }

    @PutMapping("/update")
    public void updateMovieYear(@RequestBody UpdateMovieDTO body) throws Exception {

        var movie = moviesRepository.findById(body.id()).get();
System.out.println("movie=" + movie);
movie.setTitle(body.title);
movie.setPrice(body.price);
movie.getTimes().clear();
movie.getTimes().addAll(body.times());
movie.getYears().clear();
movie.getYears().addAll(List.of(body.year()));
        moviesRepository.save(movie);

        
        // envoyer une notification mail lorsque certains films intègrent la base de données, le movie theater attendait des films de cette époque avec impatience et tenait donc à être informé par mail
        if (body.year() >= 1980 && body.year() < 1990) {
            envoyerEmail("nouveau film des années 80",
                    "Le film " + movie.getTitle() + "(" + body.year() + ")" + " a été intégré");
        } else if (body.year() == 1977) {
            envoyerEmail("un film de 1977 est rentré",
                    "Le film " + movie.getTitle() + " de l'année 1977 suivie par mail a été intégré");
        }

        // supprime le film des collections dans lesquelles il était
        var matchingCollections = collections .findByMoviesTitleContainingIgnoreCase(movie.getTitle());
        for (MovieCollection c : matchingCollections) {
            c.movies.remove(movie);
            collections.save(c);
        }
        
        // ajouter le film à une collection
        String vieuxFilmCOllectionName  = "vieux films";
        if (body.year() < 1950) {
            System.out.println("un vieux film est disponible");

            List<MovieCollection>collectionsVieuxFilm = collections.findByTitleContainingIgnoreCase(vieuxFilmCOllectionName);
            
            MovieCollection collectionVieuxFilm;
            if (collectionsVieuxFilm.isEmpty()) {
                collectionVieuxFilm = new MovieCollection(vieuxFilmCOllectionName);
            } else {
                collectionVieuxFilm = collectionsVieuxFilm.get(0);
                
            }
            collectionVieuxFilm.movies.add(movie);
                collections.save(collectionVieuxFilm);
            
        } else if (body.year() == LocalDate.now().getYear()) {
            System.out.println("un film de l'année est disponible");


            List<MovieCollection>collectionsFilmDeLannee = collections.findByTitleContainingIgnoreCase("film de l'année");
            
            MovieCollection targetCollection;
            if (collectionsFilmDeLannee.isEmpty()) {
                targetCollection = new MovieCollection("film de l'année");
            } else {
                targetCollection = collectionsFilmDeLannee.get(0);
                
            }
            targetCollection.movies.add(movie);
                collections.save(targetCollection);
        }
        
    }

    private void envoyerEmail(String title, String body) {
        System.out.println("in production, this will sent an email, I promise: " + title);
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
        List<Movie> movie = moviesRepository.findByTitleContainingIgnoreCase(title);
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
        return moviesRepository.findById(id);
    }

    @GetMapping("")
    List<Movie> getAllMovies() {
        return moviesDatabase.getAllMovies();
    }

}
