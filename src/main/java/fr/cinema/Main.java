package fr.cinema;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenue cher client, quel film voulez vous voir ?");
        String movieName = System.console().readLine();

        System.out.println("Recherche du film: " + movieName);

        var moviesDb = new MoviesDatabase();

        try {
            String r = moviesDb.getMovieInfo(movieName);
            System.out.println("La ligne du film: " + r);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
