package com.citronix.backend.dto.farm.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateFarmRequest {
    @NotBlank(message = "Farm name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Total area is required")
    @Positive(message = "Total area must be positive")
    @DecimalMin(value = "10000", message = "Farm area must be at least 10 000 m²")
    private Double totalAreaInSquareMeters;
}