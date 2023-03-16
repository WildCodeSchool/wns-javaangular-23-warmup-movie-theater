package fr.cinema.domain.events;

import fr.cinema.domain.model.Movie;

public record MovieRegisteredEvent(Movie movie) implements DomainEvent {
    
}
