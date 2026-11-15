package pt.rumos.mymovies.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> systemGetStats(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(Map.of(
                "status", "Operational",
                "requestedBy", jwt.getSubject(),
                "totalMovies", 150,
                "activeUsers", 42
        ));
    }
}
