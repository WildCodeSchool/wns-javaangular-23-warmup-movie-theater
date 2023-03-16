package fr.cinema.domain.services;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cinema.domain.model.Movie;
import fr.cinema.domain.model.MovieCollection;
import fr.cinema.repositories.MoviesCollectionRepository;
import fr.cinema.repositories.MoviesRepository;

@Service
public class MoviesService {
    private static final Logger log = LoggerFactory.getLogger(MoviesService.class);

    private final EmailSender emailSender;
    private final MoviesRepository moviesRepository;
    private final MoviesCollectionRepository moviesCollectionRepository;

    @Autowired
    public MoviesService(EmailSender emailSender, MoviesRepository moviesRepository,
            MoviesCollectionRepository moviesCollectionRepository) {
        this.emailSender = emailSender;
        this.moviesRepository = moviesRepository;
        this.moviesCollectionRepository = moviesCollectionRepository;
    }

    public List<Movie> searchMoviesByTitle(String title) {
        List<Movie> movies = moviesRepository.findByTitleContainingIgnoreCase(title);
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        return moviesRepository.findById(id);
    }

    public List<Movie> getAllMovies() {
        return moviesRepository.findAll();
    }

    public void registerMovie(String title, float price, short year, List<String> times) {
        var movie = new Movie();
        movie.setPrice(price);
        movie.setTitle(title);
        movie.setYears(List.of(year));
        movie.setTimes(times);
        moviesRepository.save(movie);

        onMovieYearChanged(movie);
    }

    public void updateMovie(Long id, Optional<String> title, Optional<Float> price, Optional<Short> year,
            Optional<List<String>> times) {
        var movie = moviesRepository.findById(id).get();
        System.out.println("movie=" + movie);
        if (title.isPresent()) {
            movie.setTitle(title.get());
        }
        if (price.isPresent()) {
            movie.setPrice(price.get());
        }
        if (times.isPresent()) {
            movie.getTimes().clear();
            movie.getTimes().addAll(times.get());
        }
        if (year.isPresent()) {
            movie.getYears().clear();
            movie.getYears().addAll(List.of(year.get()));
        }
        moviesRepository.save(movie);

        if (year.isPresent()) {
            onMovieYearChanged(movie);
        }
    }


    public void loadFromCsv(Path csvFilePath) throws IOException {
        log.info("load movies from csv: " + csvFilePath);

        List<String> lines = Files.readAllLines(csvFilePath);
        // skip header
        lines.remove(0);

        List<Movie> moviesFromCsv = lines.stream().map(this::movieFromCsvLine).toList();
        moviesRepository.saveAll(moviesFromCsv);
    }

    private Movie movieFromCsvLine(String line) {
        String[] values = line.split("[;]");
        try {
            int index = 0;
            long id = Long.parseLong(values[index++]);
            String title = values[index++];
            title = title.substring(1, title.length() - 1);

            List<Short> years = Stream.of(values[index++].split("[,]"))
                    .map((String s) -> Short.parseShort(s))
                    .collect(Collectors.toList());
            float price = Float.parseFloat(values[index++]);
            List<String> times = List.of(values[index++].split("[,]"));
            return new Movie(id, title, years, price, times);
        } catch (Exception e) {
            throw new RuntimeException("malformed csv file", e);
        }
    }
}
