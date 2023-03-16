package fr.cinema.domain.events;

import fr.cinema.domain.model.Movie;

public record MovieYearChangedEvent(Movie movie) implements DomainEvent {
    
}
