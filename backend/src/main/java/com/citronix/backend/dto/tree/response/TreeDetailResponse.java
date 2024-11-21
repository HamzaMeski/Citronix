package com.citronix.backend.dto.tree.response;

import com.citronix.backend.util.constants.TreeStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TreeDetailResponse {
    private Long id;
    private LocalDate plantingDate;
    private TreeStatus status;
    private Long fieldId;
    private String fieldName;
    private int ageInYears;
    private double expectedProductivityPerSeason;
    private double totalProduction;
    private double averageProductionPerSeason;
    private List<TreeHarvestResponse> harvestHistory;
}