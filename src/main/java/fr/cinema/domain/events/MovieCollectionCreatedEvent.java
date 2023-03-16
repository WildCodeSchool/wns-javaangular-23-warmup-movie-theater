package fr.cinema.domain.events;

import fr.cinema.domain.model.Movie;
import fr.cinema.domain.model.MovieCollection;

public record MovieCollectionCreatedEvent(MovieCollection collection) implements DomainEvent {
    
}
