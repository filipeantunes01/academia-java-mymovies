package pt.rumos.mymovies.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pt.rumos.mymovies.exception.MovieNotFoundException;
import pt.rumos.mymovies.service.MovieService;
import pt.rumos.mymovies.web.error.ApiExceptionHandler;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MovieController controller = new MovieController(movieService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void getById_WhenMovieDoesNotExist_ShouldReturn404AndApiError() throws Exception {

        // Arrange: definir o comportamento do service simulado
        when(movieService.findById(99L))
                .thenThrow(new MovieNotFoundException(99L));

        // Act + Assert: simular o pedido e verificar a resposta
        mockMvc.perform(get("/api/movies/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("MOVIE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/api/movies/99"));
    }
}
