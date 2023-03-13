package fr.cinema;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class MoviesDatabase {

    public List<Movie> getAllMovies() {
        URL url = resolveDatabaseFileURL();

        try {
            URI uri = url.toURI();
            Path path = Paths.get(uri);
            List<String> lines = Files.readAllLines(path);
            // skip header
            lines.remove(0);

            return lines.stream().map(this::movieFromCsvLine).toList();
        } catch (Exception e) {
            throw new RuntimeException("cannot read all movies", e);
        }
    }

    private URL resolveDatabaseFileURL() {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource("movies.csv");
        return url;
    }

    public Movie getMovieInfo(String movieName) throws Exception {
        URL moviesDbFileUrl = resolveDatabaseFileURL();

        var scan = new Scanner(new File(moviesDbFileUrl.toURI()));

        // skip header
        scan.nextLine();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            // System.out.println(line);
            if (line.contains(movieName)) {
                return movieFromCsvLine(line);
            }
        }

        return null;
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
