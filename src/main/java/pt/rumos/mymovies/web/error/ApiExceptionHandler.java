package pt.rumos.mymovies.web.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.rumos.mymovies.exception.MovieNotFoundException;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleMovieNotFound(
            MovieNotFoundException exception,
            HttpServletRequest request) {

        return new ApiError(
                Instant.now(),
                404,
                "MOVIE_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        return new ApiError(
          Instant.now(),
          400,
          "VALIDATION_ERROR",
          "Invalid request body",
          request.getRequestURI()
        );
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpected(
            Exception exception,
            HttpServletRequest request) {

        log.error(
                "event=API_UNEXPECTED path={} type={}",
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception
        );

        return new ApiError(
                Instant.now(),
                500,
                "INTERNAL_ERROR",
                "Unexpected error",
                request.getRequestURI()
        );
    }
}
