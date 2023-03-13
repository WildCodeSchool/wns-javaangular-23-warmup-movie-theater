# Warm up exercise: Movie Theater

Vous incarnez une salle de cinéma qui propose des séances pour différents films, avec des prix et horaires spécifiques.

Nous allons créer un petit programme qui permet aux clients de savoir à quelle heure il peut voir un film, et pour quel prix.

Le code source devra être déposé dans le dossier `src/main/java`. Nous allons développer dans le package `fr.cinema` donc les fichiers `.java` seront situés dans `src/main/java/fr/cinema`

La liste des films est stockée dans le fichier `src/main/resources/movies.csv`. Le format CSV est simple, il est expliqué ici : https://fr.wikipedia.org/wiki/Comma-separated_values

Comme l'en tête du fichier l'indique, les colonnes sont ID, Titre, Année(s), Prix et Horaires, ce qui nous permettra d'accomplir l'exercice

Note : il existe `src/main/java/fr/cinema/package-info.java`. Il n'a qu'un but descriptif et n'a pas d'impact sur le projet. 

## Pré-requis
Vous devez avoir VSCode avec le "Extension Pack for Java" et être capable de compiler un programme Java, et de l'exécuter

## Phase 1

### 1.1 Préparer l'environnement collaboratif

Pour cet exercice, vous allez travailler en petits groupes. Dans le but de pouvoir collaborer efficacement, vous allez pouvoir tester la puissance de VSCode Live Share.

L'un des membres du groupe fork et clone ce repository et l'ouvre dans VSCode.

Puis suivez ce tutoriel pour mettre en place une session collaborative (vous pouvez vous connecter à LiveShare en utilisant votre compte GitHub) :
https://code.visualstudio.com/learn/collaboration/live-share#_get-started-with-live-share


Cette étape est validée lorsque plusieurs membres de votre groupe peuvent modifier en même temps un fichier sur VSCode (ce readme par exemple :))


### 1.2 Créer un programme basique
Créer une classe `Main` (dans un nouveau fichier `Main.java` donc) avec une méthode `main` affichant une simple ligne dans la console "Bienvenue cher client, quel film voulez vous voir ?"

### 1.3 Lire la demande du client
Améliorer le programme pour que le client puisse saisir dans le terminal un nom de film.

Le programme doit lire ce que l'utilisateur a rentré, et afficher une nouvelle ligne "Recherche du film [Remettre ce que l'utilisateur a saisi]"

Cet article explique comment lire depuis la console (Méthode 3 recommandée)
https://www.geeksforgeeks.org/ways-to-read-input-from-console-in-java/

### 1.4 Accéder au fichier des films
Créer une nouvelle classe `MoviesDatabase` qui contient une méthode `String readAllMovies()`, permettant de lire le contenu du fichier des films.

Le main va créer une instance (new) de la classe `MoviesDatabase`, et appeler la méthode readAllMovies pour afficher le contenu du fichier dans la console.

Cette étape est validée lorsque le programme, en plus de lire le nom du film, affiche le contenu du fichier.

https://pieces-of-code.com/guide/howto/java-files-read-write.html#fichiers-textes

### 1.5 Récupérer la ligne d'un seul film
Améliorer la classe `MoviesDatabase` pour rajouter une méthode `String getMovieInfo(String movieName)`.

Cette étape est plus compliquée, il faut parcourir le contenu du fichier, ligne par ligne, pour chercher le nom du film passé en paramètre de la méthode, puis retourner uniquement la ligne correspondant au bon film.

Cette étape est validée lorsque le programme, au lieu d'afficher tout le fichier dans la console, n'affiche que la ligne correspondant au nom de film entré par l'utilisateur

Aide : 
* https://www.delftstack.com/fr/howto/java/how-to-read-a-large-text-file-line-by-line-in-java/
* https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/String.html#contains(java.lang.CharSequence)
* https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/String.html#indexOf(java.lang.String)

### 1.6 Créer une classe Movie
Créer une nouvelle classe `Movie` pour stocker les infos du film. Nous aurons donc les champs suivants : 
* id -> nombre
* title -> chaîne de caractère
* years -> liste de nombre
* price -> nombre
* times -> liste de chaînes

Cette classe doit implémenter la méthode `toString()` pour afficher de façon élégante les informations du film.

Cette étape est validée lorsque la méthode `MoviesDatabase.getMovieInfo(..)` retourne une instance de `Movie` et non une `String`

## Phase 2

### 2.1 Créer une classe de controller REST

Créer une classe MoviesController annotée avec RestController

Ce controller doit comporter 3 méthodes : 
* `List<Movie> getAllMovies()`
* `Movie getMovieByName(String name)`
* `Movie getMovieById(Long id)`

Tous ces endpoints répondent en méthode GET et retourne du JSON

Le paramètre de getMovieByName est passé dans la query string (RequestParam)
Le paramètre de getMovieById est passé dans le chemin (PathParam)

Ils doivent tous avoir une javadoc, exemple ci dessous

```java
/**
 * Ceci est une javadoc
 * @param param1 doc du paramètre
 */
public String maMethode(int param1) {
    // ...
}
```

### 2.2 Générer la documentation OpenAPI de la classe de controller

Ajouter la dépendance requise pour consulter la documentation à l'adresse http://localhost:8080/swagger-ui/index.html

## Phase 3

### 3.1 Créer une base de données pour le movie theater

Créer une base de données avec le SGBD de votre choix, ajouter JPA en dépendance du projet et configurer JPA pour y accéder 

### 3.2 Créer l'entité Movie et son repository

La classe Movie doit être une entité, donc utilisable par JPA pour faire le lien avec la BDD

Créer le repository qui va avec

### 3.3 Insérer des données dans la BDD

A chaque démarrage de l'application, une méthode doit être appelée pour :
* vider le contenu actuel de la table Movie (le repository permet de faire ça)
* lire le fichier CSV à l'aide du class loader
* créer un `Movie` pour chaque ligne du fichier CSV et le sauvegarder à l'aide du repositoru
* la sauvegarde de plusieurs films doit être optimisée, et donc utiliser un insert en mode "batch" (opération efficace sur de multiple entrées), voir lien
  * https://www.baeldung.com/spring-data-jpa-batch-inserts#howto


Aide : 
* https://www.baeldung.com/running-setup-logic-on-startup-in-spring

### 3.4 Rajouter des endpoints 

Créer les endpoints permettant 
* de récupérer les films en passant juste une partie d'un titre de film (sans tenir compte de la casse)
* de récupérer un film par son ID
* *Optionnel* de récupérer les films dont la date de sortie est ultérieur à une date passée en paramètre

### 3.5 Rajouter une vue permettant de saisir un nouveau film 

Créer une vue Thymeleaf permettant de saisir un nouveau film dans la base de données de cette application

### 3.6 Rajouter un endpoint pour insérer N nouveaux films aléatoires

Se servir de cette API :  
https://story-shack-cdn-v2.glitch.me/generators/random-movie-generator?count=25

Aide : 
* Comment faire une requête HTTP en Java : https://www.baeldung.com/java-9-http-client

