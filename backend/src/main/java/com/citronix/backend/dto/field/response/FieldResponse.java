package com.citronix.backend.dto.field.response;

import lombok.Data;

@Data
public class FieldResponse {
    private Long id;
    private Double areaInSquareMeters;
    private Long farmId;
    private String farmName;
    private int numberOfTrees;
    private int maxTreeCapacity;
    private Double occupancyRate;
}