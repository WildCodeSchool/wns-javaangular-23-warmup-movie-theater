package fr.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import java.util.List;
import fr.cinema.repositories.MoviesRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class DemoOpenapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOpenapiApplication.class, args);
    }

    @Autowired
    MoviesRepository moviesRepository;

    @Autowired
    MoviesDatabase moviesDatabase;

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("=======> TP ====> APPLICATION STARTED");

        moviesRepository.deleteAll();

        var movie = new Movie();
        movie.setPrice(20);
        movie.setTitle("Matrix");

        Short year = 1990;
        movie.setYears(List.of(year));

        movie.setTimes(List.of("18:00", "22:00"));

        moviesRepository.save(movie);

        // =====

        List<Movie> moviesFromCsv = moviesDatabase.getAllMovies();
        moviesRepository.saveAll(moviesFromCsv);
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Correction de l'atelier OpenAPI 8 mars")
                        .description("C'était une des manières de faire mais il n'y en a pas qu'une"));
    }

    /**
     * System.out.println("Bienvenue cher client, quel film voulez vous voir ?");
     * String movieName = System.console().readLine();
     * 
     * System.out.println("Recherche du film: " + movieName);
     * 
     * var moviesDb = new MoviesDatabase();
     * 
     * try {
     * String r = moviesDb.getMovieInfo(movieName);
     * System.out.println("La ligne du film: " + r);
     * } catch (Exception e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * }
     */
}
