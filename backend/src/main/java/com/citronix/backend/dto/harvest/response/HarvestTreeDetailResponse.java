package com.citronix.backend.dto.harvest.response;

import com.citronix.backend.util.constants.TreeStatus;
import lombok.Data;

@Data
public class HarvestTreeDetailResponse {
    private Long id;
    private Long harvestId;
    private Long treeId;
    private Double quantity;
    private String fieldName;
    private String farmName;
    private TreeStatus treeStatus;
    private int treeAge;
}