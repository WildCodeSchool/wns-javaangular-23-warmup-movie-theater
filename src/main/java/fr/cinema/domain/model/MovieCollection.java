package fr.cinema.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cinema.domain.events.DomainEventPublisher;
import fr.cinema.domain.events.MovieCollectionCreatedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class MovieCollection {

    private final static Logger log = LoggerFactory.getLogger(MovieCollection.class);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
    
    public void create(String title) {
        log.info("MovieCollection.create requested - title=" + title);
        setTitle(title);
        DomainEventPublisher.getInstance().publishEvent(new MovieCollectionCreatedEvent(this));
        log.info("MovieCollection.create event done");
    }


}