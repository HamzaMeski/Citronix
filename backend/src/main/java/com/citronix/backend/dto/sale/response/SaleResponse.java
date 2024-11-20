package com.citronix.backend.dto.sale.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SaleResponse {
    private Long id;
    private LocalDate saleDate;
    private Double unitPrice;
    private String client;
    private Long harvestId;
    private Double quantity;
    private Double totalRevenue;
}