package fr.cinema.domain.commands;

import java.util.List;

import fr.cinema.domain.model.Movie;
import jakarta.transaction.Transactional;

public record CreateMovieCommand(String title, float price, short year, List<String> times) implements DomainCommand {
    @Override
    public void execute() {
        System.out.println("execute create movie comand");
        var movie = new Movie();
        movie.setPrice(price);
        movie.setTitle(title);
        movie.setYears(List.of(year));
        movie.setTimes(times);

        movie.create();
    }
}
