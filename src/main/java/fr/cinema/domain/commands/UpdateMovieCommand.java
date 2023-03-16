package fr.cinema.domain.commands;

import java.util.List;
import java.util.Optional;

import fr.cinema.domain.model.Movie;

public record UpdateMovieCommand(Movie movie, Optional<String> title, Optional<Float> price, Optional<Short> year,
Optional<List<String>> times) implements DomainCommand {
    @Override
    public void execute() {
        System.out.println("execute update movie comand");
        System.out.println("movie=" + movie);
        
        movie.changeInformation(title, price, year, times);
    }
}
