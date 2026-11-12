package pt.rumos.mymovies.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

import pt.rumos.mymovies.model.Movie;
import pt.rumos.mymovies.service.MovieService;
import pt.rumos.mymovies.dto.MovieRequest;
import pt.rumos.mymovies.dto.MovieResponse;
import pt.rumos.mymovies.dto.RatingUpdateRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }


    /* ----- GET -----  */

    // Listar todos
    @GetMapping
    public ResponseEntity<List<MovieResponse>> listAll() {

        List<MovieResponse> response = service.findAll();
        return ResponseEntity.ok(response);
    }


    // Pesquisar por ID com parâmetro de caminho / URI
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }


    // Pesquisar por parâmetro de consulta / query string params
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchByTitle(
            @RequestParam(required = false) String title) {

        if (title == null || title.isBlank()) {
            List<MovieResponse> response = service.findAll();
            return ResponseEntity.ok(response);
        }

        List<MovieResponse> result = service.findByTitle(title);

        return ResponseEntity.ok(result);
    }


    /* ----- POST ----- */
    @PostMapping
    public ResponseEntity<MovieResponse> create(
            @Valid @RequestBody MovieRequest request) {

        MovieResponse created = service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()                   // Obtém o endereço do pedido atual: /api/movies.
                .path("/{id}")                          // Acrescenta a parte variável /{id}.
                .buildAndExpand(created.id())           // Substitui {id} pelo identificador do filme criado.
                .toUri();                               // Produz um objeto Java do tipo URI.

        return ResponseEntity.created(location).body(created);
    }


    /* ----- PUT ----- */
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest request ) {

        Optional<MovieResponse> updated = service.update(id, request);

        return updated.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(updated.get());
    }


    /* ----- PATCH ----- */
    @PatchMapping("/{id}/rating")
    public ResponseEntity<MovieResponse> updateRating(
            @PathVariable Long id,
            @Valid @RequestBody RatingUpdateRequest request) {

        Optional<MovieResponse> updated = service.updateRating(id, request);

        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated.get());
    }


    /* ----- DELETE ----- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        boolean deleted = service.delete(id);

        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
