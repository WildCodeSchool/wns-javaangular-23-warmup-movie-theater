package fr.cinema.domain.commands;

import java.util.List;
import java.util.Optional;

import fr.cinema.domain.events.DomainEventPublisher;
import fr.cinema.domain.events.MovieUpdatedEvent;
import fr.cinema.domain.model.Movie;
import jakarta.transaction.Transactional;

public record UpdateMovieCommand(Movie movie, Optional<String> title, Optional<Float> price, Optional<Short> year,
Optional<List<String>> times) implements DomainCommand {
    @Override
    public void execute() {
        System.out.println("execute update movie comand");
        System.out.println("movie=" + movie);
        if (title.isPresent()) {
            movie.setTitle(title.get());
        }
        if (price.isPresent()) {
            movie.setPrice(price.get());
        }
        if (times.isPresent()) {
            movie.getTimes().clear();
            movie.getTimes().addAll(times.get());
        }
        if (year.isPresent()) {
            movie.getYears().clear();
            movie.getYears().addAll(List.of(year.get()));
        }
        DomainEventPublisher.getInstance().publishEvent(new MovieUpdatedEvent(movie));

        if (year.isPresent()) {
            onMovieYearChanged(movie);
        }
    }
}
