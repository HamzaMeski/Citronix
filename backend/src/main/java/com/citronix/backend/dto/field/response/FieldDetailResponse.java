package com.citronix.backend.dto.field.response;

import com.citronix.backend.dto.tree.response.TreeResponse;
import lombok.Data;
import java.util.List;

@Data
public class FieldDetailResponse {
    private Long id;
    private Double areaInSquareMeters;
    private Long farmId;
    private String farmName;
    private List<TreeResponse> trees;
    private int numberOfTrees;
    private int maxTreeCapacity;
    private Double occupancyRate;
    private Double productivityPerSquareMeters;
}