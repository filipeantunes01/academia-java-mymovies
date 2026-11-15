package pt.rumos.mymovies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Consultas de filmes públicos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/movies",
                                "/api/movies/**"
                        ).permitAll()

                        // Monitorização pública do laboratório
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/metrics",
                                "/actuator/metrics/**"
                        ).permitAll()

                        // Consola H2 apenas para desenvolvimento local
                        .requestMatchers("/h2-console/**").permitAll()

                        // Administração exige permissão específica
                        .requestMatchers("/api/admin/**")
                        .hasAuthority("SCOPE_movies.admin")

                        // Restantes operações exigem autenticação
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                )

                .headers(headers -> headers.
                        frameOptions(frame -> frame.sameOrigin())
                )

                .build();
    }
}
