package com.citronix.backend.dto.harvest.response;

import com.citronix.backend.util.constants.Season;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HarvestResponse {
    private Long id;
    private LocalDate harvestDate;
    private Season season;
    private Double totalQuantity;
    private int numberOfTrees;
    private Double averageQuantityPerTree;
    private Double availableQuantity;
}