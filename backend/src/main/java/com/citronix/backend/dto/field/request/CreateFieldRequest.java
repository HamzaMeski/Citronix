package com.citronix.backend.dto.field.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class CreateFieldRequest {
    @NotNull(message = "Farm ID is required")
    private Long farmId;

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be positive")
    @DecimalMin(value = "0.1", message = "Field area must be at least 0.1 hectares")
    private Double areaInHectares;
}