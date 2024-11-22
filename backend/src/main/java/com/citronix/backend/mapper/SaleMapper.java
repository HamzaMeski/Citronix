package com.citronix.backend.mapper;

import com.citronix.backend.dto.sale.request.CreateSaleRequest;
import com.citronix.backend.dto.sale.response.SaleResponse;
import com.citronix.backend.entity.Sale;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "harvest", ignore = true)
    @Mapping(target = "totalRevenue", expression = "java(request.getQuantity() * request.getUnitPrice())")
    Sale toEntity(CreateSaleRequest request);

    @Mapping(target = "harvestId", source = "harvest.id")
    @Mapping(target = "totalRevenue", expression = "java(sale.getQuantity() * sale.getUnitPrice())")
    SaleResponse toResponse(Sale sale);

    @AfterMapping
    default void calculateTotalRevenue(@MappingTarget Sale sale) {
        sale.setTotalRevenue(sale.getQuantity() * sale.getUnitPrice());
    }
}   