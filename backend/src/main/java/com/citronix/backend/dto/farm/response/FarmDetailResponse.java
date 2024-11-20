package com.citronix.backend.dto.farm.response;

import com.citronix.backend.dto.field.response.FieldResponse;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class FarmDetailResponse {
    private Long id;
    private String name;
    private String location;
    private Double totalAreaInHectares;
    private LocalDate creationDate;
    private List<FieldResponse> fields;
    private Double usedAreaInHectares;
    private Double availableAreaInHectares;
    private int totalTrees;
    private Double totalProduction;
}