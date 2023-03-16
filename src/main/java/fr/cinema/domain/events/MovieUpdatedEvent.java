package fr.cinema.domain.events;

import fr.cinema.domain.model.Movie;

public record MovieUpdatedEvent(Movie movie) implements DomainEvent {
    
}
