package fr.cinema.domain.commands;

import fr.cinema.domain.model.MovieCollection;

public record CreateMoviesCollectionCommand(String title) implements DomainCommand {
    @Override
    public void execute() {
        System.out.println("execute create movie collection comand");
        var collection = new MovieCollection();
        collection.create(title);
    }
}
