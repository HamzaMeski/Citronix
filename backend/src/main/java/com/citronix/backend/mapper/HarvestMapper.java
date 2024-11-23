package com.citronix.backend.mapper;

import com.citronix.backend.dto.harvest.request.CreateHarvestRequest;
import com.citronix.backend.dto.harvest.response.HarvestResponse;
import com.citronix.backend.dto.harvest.response.HarvestDetailResponse;
import com.citronix.backend.dto.harvest.response.HarvestTreeDetailResponse;
import com.citronix.backend.entity.Harvest;
import com.citronix.backend.entity.HarvestDetail;
import org.mapstruct.*;

import java.time.Period;

@Mapper(componentModel = "spring", uses = {SaleMapper.class})
public interface HarvestMapper {

    @Mapping(target = "harvestDetails", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "totalQuantity", constant = "0.0")
    Harvest toEntity(CreateHarvestRequest request);

    @Mapping(target = "numberOfTrees", expression = "java(harvest.getHarvestDetails().size())")
    @Mapping(target = "averageQuantityPerTree", expression = "java(calculateAverageQuantity(harvest))")
    HarvestResponse toResponse(Harvest harvest);

    @Mapping(target = "treeDetails", source = "harvestDetails")
    @Mapping(target = "sales", source = "sales")
    @Mapping(target = "totalRevenue", expression = "java(calculateTotalRevenue(harvest))")
    @Mapping(target = "averagePricePerKg", expression = "java(calculateAveragePricePerKg(harvest))")
    HarvestDetailResponse toDetailResponse(Harvest harvest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "harvestId", source = "harvest.id")
    @Mapping(target = "treeId", source = "tree.id")
    @Mapping(target = "farmName", source = "tree.field.farm.name")
    @Mapping(target = "fieldId", source = "tree.field.id")
    @Mapping(target = "treeStatus", source = "tree.status")
    @Mapping(target = "treeAge", expression = "java(calculateTreeAge(detail))")
    HarvestTreeDetailResponse toTreeDetailResponse(HarvestDetail detail);

    default Double calculateAverageQuantity(Harvest harvest) {
        if (harvest.getHarvestDetails().isEmpty()) return 0.0;
        return harvest.getTotalQuantity() / harvest.getHarvestDetails().size();
    }

    default Double calculateTotalRevenue(Harvest harvest) {
        return harvest.getSales().stream()
                .mapToDouble(sale -> sale.getQuantity() * sale.getUnitPrice())
                .sum();
    }

    default Double calculateAveragePricePerKg(Harvest harvest) {
        double totalRevenue = calculateTotalRevenue(harvest);
        return harvest.getTotalQuantity() > 0 ? totalRevenue / harvest.getTotalQuantity() : 0.0;
    }

    default int calculateTreeAge(HarvestDetail detail) {
        return Period.between(detail.getTree().getPlantingDate(), detail.getHarvest().getHarvestDate())
                .getYears();
    }
} 