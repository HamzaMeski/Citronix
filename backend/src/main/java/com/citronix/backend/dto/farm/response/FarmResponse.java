package com.citronix.backend.dto.farm.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FarmResponse {
    private Long id;
    private String name;
    private String location;
    private Double totalAreaInHectares;
    private LocalDate creationDate;
    private int numberOfFields;
    private Double usedAreaInHectares;
    private Double availableAreaInHectares;
}