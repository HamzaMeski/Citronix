package com.citronix.backend.dto.tree.response;

import com.citronix.backend.util.constants.Season;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TreeHarvestResponse {
    private Long harvestId;
    private LocalDate harvestDate;
    private Season season;
    private Double quantity;
} 