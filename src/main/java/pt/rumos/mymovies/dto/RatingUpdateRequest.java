package pt.rumos.mymovies.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record RatingUpdateRequest(
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("10.0")
        Double rating
) {}
