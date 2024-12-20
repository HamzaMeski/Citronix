package com.citronix.backend.dto.farm.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class FarmResponse {
    private Long id;
    private String name;
    private String location;
    private Double totalAreaInSquareMeters;
    private LocalDate creationDate;
    private int numberOfFields;
    private Double usedAreaInSquareMeters;
    private Double availableAreaInSquareMeters;
}