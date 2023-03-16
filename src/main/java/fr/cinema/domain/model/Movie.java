package fr.cinema.domain.model;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cinema.domain.events.DomainEventPublisher;
import fr.cinema.domain.events.MovieRegisteredEvent;
import fr.cinema.domain.events.MovieUpdatedEvent;
import fr.cinema.domain.events.MovieYearChangedEvent;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {
  
    private final static Logger log = LoggerFactory.getLogger(Movie.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ElementCollection // 1
    @CollectionTable(name = "movie_years") // 2
    @Column(name = "year") // 3
    private List<Short> years;
    private float price;

    @ElementCollection // 1
    @CollectionTable(name = "movie_times") // 2
    @Column(name = "times") // 3
    private List<String> times;

    public Movie() {
    }

    public Movie(Long id, String title, List<Short> years, float price, List<String> times) {
        this.id = id;
        this.title = title;
        this.years = years;
        this.price = price;
        this.times = times;
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

    public List<Short> getYears() {
        return years;
    }

    public void setYears(List<Short> years) {
        this.years = years;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public void create(String title, float price, short year, List<String> times) {
        log.info("Movie.create requested - title=" + title);
        setPrice(price);
        setTitle(title);
        setYears(List.of(year));
        setTimes(times);
        DomainEventPublisher.getInstance().publishEvent(new MovieRegisteredEvent(this));
        log.info("Movie.create event done");
    }

    public void changeInformation(Optional<String> title, Optional<Float> price, Optional<Short> year,
            Optional<List<String>> times) {
        log.info("Movie.changeInformation requested");
        if (title.isPresent()) {
            setTitle(title.get());
        }
        if (price.isPresent()) {
            setPrice(price.get());
        }
        if (times.isPresent()) {
            getTimes().clear();
            getTimes().addAll(times.get());
        }
        if (year.isPresent()) {
            getYears().clear();
            getYears().addAll(List.of(year.get()));
        }
        DomainEventPublisher.getInstance().publishEvent(new MovieUpdatedEvent(this));
        log.info("Movie.changeInformation event published");
        if (year.isPresent()) {
            DomainEventPublisher.getInstance().publishEvent(new MovieYearChangedEvent(this));
            log.info("Movie.changeInformation YEAR CHANGED event published");
        }
    }

}
