package com.citronix.backend.dto.harvest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddHarvestDetailRequest {
    @NotNull(message = "Tree ID is required")
    private Long treeId;

    @NotNull(message = "Harvest ID is required")
    private Long harvestId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;
}