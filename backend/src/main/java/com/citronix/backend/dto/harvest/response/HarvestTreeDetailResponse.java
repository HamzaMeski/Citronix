package com.citronix.backend.dto.harvest.response;

import lombok.Data;

@Data
public class HarvestTreeDetailResponse {
    private Long treeId;
    private Double quantity;
    private String fieldName;
    private String farmName;
}