package fr.cinema.domain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fr.cinema.domain.events.MovieRegisteredEvent;
import fr.cinema.domain.events.MovieYearChangedEvent;
import fr.cinema.domain.services.EmailSender;

@Component
public class Notifier {
    private static final Logger log = LoggerFactory.getLogger(Notifier.class);

    private final EmailSender emailSender;

    @Autowired
    public Notifier(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @EventListener
    @Order(100)
    @Async 
    public void onMovieRegistered(MovieRegisteredEvent event) {
        sendNotificationIfRelevant(event.movie());
    }
    @EventListener
    @Order(100)
    @Async 
    public void onMovieYearChanged(MovieYearChangedEvent event) {
        sendNotificationIfRelevant(event.movie());
    }

    private void sendNotificationIfRelevant(Movie movie) {
        log.info("movie year changed: check notifications");
        for (short year : movie.getYears()) {
            if (year >= 1980 && year < 1990) {
                emailSender.sendEmail("nouveau film des années 80",
                        "Le film " + movie.getTitle() + "(" + year + ")" + " a été intégré");
            } else if (year == 1977) {
                emailSender.sendEmail("un film de 1977 est rentré",
                        "Le film " + movie.getTitle() + " de l'année 1977 suivie par mail a été intégré");
            }
        }
    }
}
