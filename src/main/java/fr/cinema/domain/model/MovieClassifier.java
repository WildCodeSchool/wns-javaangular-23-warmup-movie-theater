package fr.cinema.domain.model;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import fr.cinema.domain.events.MovieRegisteredEvent;
import fr.cinema.repositories.MoviesCollectionRepository;
import fr.cinema.repositories.MoviesRepository;

@Component
public class MovieClassifier {
    private static final Logger log = LoggerFactory.getLogger(MovieClassifier.class);

    private final MoviesRepository moviesRepository;
    private final MoviesCollectionRepository moviesCollectionRepository;

    @Autowired
    public MovieClassifier(MoviesRepository moviesRepository,
            MoviesCollectionRepository moviesCollectionRepository) {
        this.moviesRepository = moviesRepository;
        this.moviesCollectionRepository = moviesCollectionRepository;
    }

    @EventListener
    @Order(50)
    public void onMovieRegistered(MovieRegisteredEvent event) {
        onMovieYearChanged(event.movie());
    }

    private void onMovieYearChanged(Movie movie) {
        System.out.println("movie year changed: check classification");
        for (short year : movie.getYears()) {
            var matchingCollections = this.moviesCollectionRepository
                    .findByMoviesTitleContainingIgnoreCase(movie.getTitle());
            for (MovieCollection c : matchingCollections) {
                c.getMovies().remove(movie);
                this.moviesCollectionRepository.save(c);
            }

            // ajouter le film à une collection
            String vieuxFilmCOllectionName = "vieux films";
            if (year < 1950) {
                System.out.println("un vieux film est disponible");

                List<MovieCollection> collectionsVieuxFilm = this.moviesCollectionRepository
                        .findByTitleContainingIgnoreCase(vieuxFilmCOllectionName);

                MovieCollection collectionVieuxFilm;
                if (collectionsVieuxFilm.isEmpty()) {
                    collectionVieuxFilm = new MovieCollection(vieuxFilmCOllectionName);
                } else {
                    collectionVieuxFilm = collectionsVieuxFilm.get(0);

                }
                collectionVieuxFilm.getMovies().add(movie);
                this.moviesCollectionRepository.save(collectionVieuxFilm);

            } else if (year == LocalDate.now().getYear()) {
                System.out.println("un film de l'année est disponible");

                List<MovieCollection> collectionsFilmDeLannee = this.moviesCollectionRepository
                        .findByTitleContainingIgnoreCase("film de l'année");

                MovieCollection targetCollection;
                if (collectionsFilmDeLannee.isEmpty()) {
                    targetCollection = new MovieCollection("film de l'année");
                } else {
                    targetCollection = collectionsFilmDeLannee.get(0);

                }
                targetCollection.getMovies().add(movie);
                this.moviesCollectionRepository.save(targetCollection);
            }
        }
    }
}
