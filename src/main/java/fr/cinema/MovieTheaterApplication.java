package fr.cinema;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import fr.cinema.domain.model.Movie;
import fr.cinema.domain.services.MoviesService;
import fr.cinema.repositories.MoviesCollectionRepository;
import fr.cinema.repositories.MoviesRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class MovieTheaterApplication {

    private static final Logger log = LoggerFactory.getLogger(MovieTheaterApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MovieTheaterApplication.class, args);
    }

    @Autowired
    MoviesRepository moviesRepository;

    @Autowired
    MoviesCollectionRepository moviesCollectionRepository;

    @Autowired
    MoviesService moviesService;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("=======> MOVIE THEATER --- APPLICATION STARTED");

        log.info("clean existing data from DB");
        moviesCollectionRepository.deleteAll();
        moviesRepository.deleteAll();

        log.info("inject data from CSV to DB");
        try {

            ClassLoader loader = getClass().getClassLoader();
            URL url = loader.getResource("movies.csv");
            URI uri = url.toURI();
            Path csvFilePath = Paths.get(uri);
            moviesService.loadFromCsv(csvFilePath);
        } catch (Exception e) {
            log.error("cannot load movies from CSV", e);
            throw new RuntimeException("cannot load movies from CSV", e);
        }

        log.info("=======> MOVIE THEATER --- INIT COMPLETE");
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Gestion de films")
                        .description("L'architecture de l'application a été revue"));
    }
}
