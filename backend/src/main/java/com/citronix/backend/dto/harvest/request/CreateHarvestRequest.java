package com.citronix.backend.dto.harvest.request;

import com.citronix.backend.util.constants.Season;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateHarvestRequest {
    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;

    @NotNull(message = "Season is required")
    private Season season;
}