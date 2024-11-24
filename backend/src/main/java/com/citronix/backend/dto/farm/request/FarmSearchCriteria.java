package com.citronix.backend.dto.farm.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FarmSearchCriteria {
    private String name;
    private String location;
    private Double minTotalArea;
    private Double maxTotalArea;
    private LocalDate createdAfter;
    private LocalDate createdBefore;
    private Integer minFields;
    private Integer maxFields;
    private Integer minTrees;
    private Integer maxTrees;
} 