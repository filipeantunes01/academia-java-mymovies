package pt.rumos.mymovies.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.rumos.mymovies.dto.MovieResponse;
import pt.rumos.mymovies.exception.MovieNotFoundException;
import pt.rumos.mymovies.model.Movie;
import pt.rumos.mymovies.repository.MovieRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void findById_WhenMovieExists_ShouldReturnMovieResponse() {

        // 1. Preparar os dados
        Movie movie = new Movie(
                1L,
                "Interstellar",
                "Christopher Nolan",
                2014,
                8.7
        );

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        // 2. Executar o método que queremos testar
        MovieResponse result = movieService.findById(1L);

        // 3. Verificar os resultados
        assertEquals(1L, result.id());
        assertEquals("Interstellar", result.title());
        assertEquals(2014, result.year());

        verify(movieRepository, times(1)).findById(1L);

        // Este padrão é conhecido como Arrange → Act → Assert
    }

    @Test
    void findById_WhenMovieDoesNotExist_ShouldThrowMovieNotFoundException() {

        when(movieRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> movieService.findById(99L)
        );

        verify(movieRepository, times(1)).findById(99L);
    }
}
