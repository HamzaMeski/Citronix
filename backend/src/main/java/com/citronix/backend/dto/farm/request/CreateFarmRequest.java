package com.citronix.backend.dto.farm.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateFarmRequest {
    @NotBlank(message = "Farm name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Total area is required")
    @Positive(message = "Total area must be positive")
    @DecimalMin(value = "1.0", message = "Farm area must be at least 1 hectare")
    private Double totalAreaInHectares;

    @NotNull(message = "Creation date is required")
    private LocalDate creationDate;
}