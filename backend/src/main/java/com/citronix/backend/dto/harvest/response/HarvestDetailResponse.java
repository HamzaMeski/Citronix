package com.citronix.backend.dto.harvest.response;

import com.citronix.backend.dto.sale.response.SaleResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class HarvestDetailResponse extends HarvestResponse {
    private List<HarvestTreeDetailResponse> treeDetails;
    private List<SaleResponse> sales;
    private Double totalRevenue;
    private Double averagePricePerKg;
}