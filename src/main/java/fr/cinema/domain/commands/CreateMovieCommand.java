package fr.cinema.domain.commands;

import java.util.List;

import fr.cinema.domain.model.Movie;

public record CreateMovieCommand(String title, float price, short year, List<String> times) implements DomainCommand {
    @Override
    public void execute() {
        System.out.println("execute create movie comand");
        var movie = new Movie();
        movie.create(title, price, year, times);
    }
}
