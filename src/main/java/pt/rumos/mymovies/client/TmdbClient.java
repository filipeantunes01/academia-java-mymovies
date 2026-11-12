package pt.rumos.mymovies.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;

import pt.rumos.mymovies.dto.tmdb.TmdbMovie;
import pt.rumos.mymovies.dto.tmdb.TmdbSearchResponse;

@Component
public class TmdbClient {

    private final RestClient client;

    // Construtor
    public TmdbClient(
            RestClient.Builder builder,
            // @Value permite obter valores definidos no application.properties
            @Value("${tmdb.base-url}") String baseUrl,
            @Value("${tmdb.token}") String token) {

        this.client = builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }


    // Faz uma pesquisa no TMDB
    public TmdbSearchResponse searchMovies(String query) {
        return client
                .get()                                              // Preparar um pedido GET
                .uri(uriBuilder -> uriBuilder              // Construir o endereço e incluir o parâmetro "query
                        .path("/search/movie")
                        .queryParam("query", query)
                        .build())
                .retrieve()                                         // Executar o pedido e preparar a leitura da resposta
                .body(TmdbSearchResponse.class);                    // Converter o JSON recebido num objeto Java daquele tipo
    }


    // Obtém os detalhes de um filme
    public TmdbMovie getMovieDetails(long tmdbId) {
        return client
                .get()
                .uri("/movie/{id}", tmdbId)
                .retrieve()
                .body(TmdbMovie.class);
    }
}
