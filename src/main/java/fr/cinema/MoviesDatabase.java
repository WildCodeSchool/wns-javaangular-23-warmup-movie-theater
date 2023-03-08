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

public class MoviesDatabase {
    public String readAllMovies() {

//        Path p = Paths.get("../resources/movies.csv");

        URL url = resolveDatabaseFileURL();

        try {
            URI uri = url.toURI();
            Path path = Paths.get(uri);
            List<String> lines = Files.readAllLines(path);
            return String.join("\n", lines);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private URL resolveDatabaseFileURL() {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource("movies.csv");
        return url;
    }

    public String getMovieInfo(String movieName) throws Exception {
        URL moviesDbFileUrl = resolveDatabaseFileURL();

        var scan = new Scanner(new File(moviesDbFileUrl.toURI()));
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            // System.out.println(line);
            if (line.contains(movieName)) {

                short year1 = 2020;
                var m = new Movie(1, "tit", 
                    List.of(year1),
                    4.4f, List.of("20:00") );

                // m.title();
                // m.years()
                System.out.println(m);

                return line;
            }
        }

        return null;
    }
}
