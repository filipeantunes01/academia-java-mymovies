package pt.rumos.mymovies.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pt.rumos.mymovies.dto.MovieRequest;
import pt.rumos.mymovies.dto.MovieResponse;
import pt.rumos.mymovies.dto.RatingUpdateRequest;
import pt.rumos.mymovies.model.Movie;
import pt.rumos.mymovies.repository.MovieRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository repository;

    // Construtor com injeção de dependências
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    // Listar todos
    @Transactional(readOnly = true)
    public List<MovieResponse> findAll() {
        List<Movie> movies = repository.findAll();

        List<MovieResponse> response = movies.stream()
                .map(MovieResponse::fromDomain)
                .toList();

        return response;
    }

    // Procurar por ID
    @Transactional(readOnly = true)
    public Optional<MovieResponse> findById(Long id) {
        Optional<Movie> result = repository.findById(id);

        Optional<MovieResponse> response = result.map(m -> MovieResponse.fromDomain(m));

        return response;
    }

    // Procurar por título
    @Transactional(readOnly = true)
    public List<MovieResponse> findByTitle(String title) {

        String term = title.trim();
        List<Movie> result = repository.findByTitleContainingIgnoreCase(term);

        return result.stream()
                .map(m -> MovieResponse.fromDomain(m))
                .toList();
    }

    // Criar registo
    @Transactional
    public MovieResponse create(MovieRequest request) {
        Movie movie = new Movie();

        movie.setTitle(request.title());
        movie.setDirector(request.director());
        movie.setYear(request.year());

        Movie saved = repository.save(movie);

        return MovieResponse.fromDomain(saved);
    }

    // Atualizar registo
    @Transactional
    public Optional<MovieResponse> update(Long id, MovieRequest request) {
        Optional<Movie> result = repository.findById(id);

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Movie movie = result.get();
        movie.setTitle(request.title());
        movie.setDirector(request.director());
        movie.setYear(request.year());

        Movie updated = repository.save(movie);
        return Optional.of(MovieResponse.fromDomain(updated));
    }

    // Atualizar rating
    @Transactional
    public Optional<MovieResponse> updateRating(Long id, RatingUpdateRequest request) {
        Optional<Movie> result = repository.findById(id);

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Movie movie = result.get();
        movie.setRating(request.rating());

        Movie saved = repository.save(movie);
        MovieResponse response = MovieResponse.fromDomain(saved);

        return Optional.of(response);
    }

    // Apagar registo
    @Transactional
    public boolean delete(Long id) {
        Optional<Movie> result = repository.findById(id);

        if (result.isEmpty()) {
            return false;
        }

        Movie movie = result.get();
        repository.delete(movie);

        /* OU DIRETAMENTE COM:
        *
        * repository.deleteById(id);
        *
        * deleteById() lida automaticamente com a ausência de um resultado, mas retorna sempre void, logo não sabemos se encontrou e apagou ou não
        *  */

        return true;
    }
}
