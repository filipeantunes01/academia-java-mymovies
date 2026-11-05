package pt.rumos.mymovies.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import pt.rumos.mymovies.dto.MovieRequest;
import pt.rumos.mymovies.dto.MovieResponse;
import pt.rumos.mymovies.dto.RatingUpdateRequest;
import pt.rumos.mymovies.model.Movie;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    // Lista fixa em memória com 22 filmes para testes e validação
    private final List<Movie> movies = new ArrayList<>(List.of(
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
    ));

    private long nextMovieId = 22L;

    /* ---------------- */
    /* ----- GET -----  */
    /* ---------------- */

    // Retornar todos
    @GetMapping
    public ResponseEntity<List<MovieResponse>> listAll() {

        List<MovieResponse> response = movies.stream()
                .map(m -> MovieResponse.fromDomain(m))
                .toList();

        return ResponseEntity.ok(response);
    }

    // Pesquisa por ID com parâmetro de caminho / URI
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {

        for (Movie movie : movies) {
            if (movie.id().equals(id)) {
                MovieResponse response = MovieResponse.fromDomain(movie);
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // Pesquisa por parâmetro de consulta / query string params
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchByTitle(
            @RequestParam(required = false) String title) {

        if (title == null || title.isBlank()) {
            List<MovieResponse> response = movies.stream()
                    .map(m -> MovieResponse.fromDomain(m))
                    .toList();

            return ResponseEntity.ok(response);
        }

        String term = title.trim().toLowerCase();

        List<MovieResponse> response = movies.stream()
                .filter(m -> m.title().toLowerCase().contains(term))
                .map(m -> MovieResponse.fromDomain(m))
                .toList();

        return ResponseEntity.ok(response);
    }


    /* ---------------- */
    /* ----- POST ----- */
    /* ---------------- */

    @PostMapping
    public ResponseEntity<MovieResponse> create(
            @Valid @RequestBody MovieRequest request) {

        Long newId = nextMovieId++;

        Movie newMovie = new Movie(
                newId,
                request.title(),
                request.director(),
                request.year()
        );

        movies.add(newMovie);

        MovieResponse response = MovieResponse.fromDomain(newMovie);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()                   // Obtém o endereço do pedido atual: /api/movies.
                .path("/{id}")                          // Acrescenta a parte variável /{id}.
                .buildAndExpand(newMovie.id())          // Substitui {id} pelo identificador do filme criado.
                .toUri();                               // Produz um objeto Java do tipo URI.

        return ResponseEntity.created(location).body(response);
    }


    /* --------------- */
    /* ----- PUT ----- */
    /* --------------- */

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest request ) {

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);

            if (movie.id().equals(id)) {

                Movie movieUpdated = new Movie(
                        id,
                        request.title(),
                        request.director(),
                        request.year(),
                        movie.rating()
                );

                movies.set(i, movieUpdated);

                MovieResponse response = MovieResponse.fromDomain(movieUpdated);

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.notFound().build();
    }


    /* ----------------- */
    /* ----- PATCH ----- */
    /* ----------------- */

    @PatchMapping("/{id}/rating")
    public ResponseEntity<MovieResponse> updateRating(
            @PathVariable Long id,
            @Valid @RequestBody RatingUpdateRequest request) {

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);

            if (movie.id().equals(id)) {

                Movie movieUpdated = new Movie(
                        movie.id(),
                        movie.title(),
                        movie.director(),
                        movie.year(),
                        request.rating()
                );

                movies.set(i, movieUpdated);

                MovieResponse response = MovieResponse.fromDomain(movieUpdated);

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.notFound().build();
    }


    /* ------------------ */
    /* ----- DELETE ----- */
    /* ------------------ */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);

            if (movie.id().equals(id)) {
                movies.remove(i);
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
}
