package pt.rumos.mymovies.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieRequest(
        @NotBlank @Size(max = 120) String title,
        @NotBlank String director,
        @NotNull @Min(1888) Integer year
) {
}
