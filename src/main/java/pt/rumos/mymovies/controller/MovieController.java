package pt.rumos.mymovies.controller;

import pt.rumos.mymovies.model.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    // Lista fixa em memória com 22 filmes para testes e validação
    private final List<Movie> movies = List.of(
            new Movie(1L, "Interstellar", "Christopher Nolan", 2014),
            new Movie(2L, "The Matrix", "Lana & Lilly Wachowski", 1999),
            new Movie(3L, "Inception", "Christopher Nolan", 2010),
            new Movie(4L, "The Dark Knight", "Christopher Nolan", 2008),
            new Movie(5L, "Pulp Fiction", "Quentin Tarantino", 1994),
            new Movie(6L, "The Godfather", "Francis Ford Coppola", 1972),
            new Movie(7L, "Fight Club", "David Fincher", 1999),
            new Movie(8L, "Forrest Gump", "Robert Zemeckis", 1994),
            new Movie(9L, "The Lord of the Rings: The Fellowship of the Ring", "Peter Jackson", 2001),
            new Movie(10L, "Star Wars: Episode V - The Empire Strikes Back", "Irvin Kershner", 1980),
            new Movie(11L, "The Matrix Reloaded", "Lana & Lilly Wachowski", 2003),
            new Movie(12L, "The Matrix Revolutions", "Lana & Lilly Wachowski", 2003),
            new Movie(13L, "Gladiator", "Ridley Scott", 2000),
            new Movie(14L, "Back to the Future", "Robert Zemeckis", 1985),
            new Movie(15L, "Jurassic Park", "Steven Spielberg", 1993),
            new Movie(16L, "Alien", "Ridley Scott", 1979),
            new Movie(17L, "Blade Runner 2049", "Denis Villeneuve", 2017),
            new Movie(18L, "Dune", "Denis Villeneuve", 2021),
            new Movie(19L, "Dune: Part Two", "Denis Villeneuve", 2024),
            new Movie(20L, "Oppenheimer", "Christopher Nolan", 2023),
            new Movie(21L, "Avatar", "James Cameron", 2009)
    );

    @GetMapping
    public List<Movie> listAll() {
        return movies;
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable Long id) {
        return movies.stream()
                .filter(m -> m.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/search")
    public List<Movie> searchByTitle(@RequestParam(required = false) String title) {
        if (title == null || title.isBlank()) {
            return movies;
        }

        String term = title.trim().toLowerCase();

        return movies.stream()
                .filter(m -> m.title().toLowerCase().contains(term))
                .toList();
    }
}
