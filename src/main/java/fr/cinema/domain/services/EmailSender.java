package fr.cinema.domain.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("mock")
public class EmailSender {
    
    public void sendEmail(String title, String body) {
        System.out.println("send EMAIL: " + title);
    }
}
