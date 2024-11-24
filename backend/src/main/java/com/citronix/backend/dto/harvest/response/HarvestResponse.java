package com.citronix.backend.dto.harvest.response;

import com.citronix.backend.util.constants.Season;
import lombok.Data;
import java.time.LocalDate;

@Data
public class HarvestResponse {
    private Long id;
    private LocalDate harvestDate;
    private Season season;
    private Double totalQuantity;
    private int numberOfTrees;
    private Double averageQuantityPerTree;
    private Double availableQuantity;
}