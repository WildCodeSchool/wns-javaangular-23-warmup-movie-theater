package fr.cinema.domain.events;

import org.springframework.context.ApplicationEventPublisher;

public class DomainEventPublisher {
    private static DomainEventPublisher instance = new DomainEventPublisher();

    private ApplicationEventPublisher publisher;

    public void setPublisher(ApplicationEventPublisher publisher) {
        System.out.println("event publisher ready");
        this.publisher = publisher;
    }

    public static DomainEventPublisher getInstance() {
        return instance;
    }
    
    public void publishEvent(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
