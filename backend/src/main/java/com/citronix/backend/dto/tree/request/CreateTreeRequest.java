package com.citronix.backend.dto.tree.request;

import com.citronix.backend.validation.PlantingSeasonValid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateTreeRequest {
    @NotNull(message = "Field ID is required")
    private Long fieldId;

    @NotNull(message = "Planting date is required")
    @PlantingSeasonValid(message = "Trees can only be planted between March and May")
    private LocalDate plantingDate;
}