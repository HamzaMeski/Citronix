package com.citronix.backend.mapper;

import com.citronix.backend.dto.farm.request.CreateFarmRequest;
import com.citronix.backend.dto.farm.request.UpdateFarmRequest;
import com.citronix.backend.dto.farm.response.FarmDetailResponse;
import com.citronix.backend.dto.farm.response.FarmResponse;
import com.citronix.backend.entity.Farm;
import com.citronix.backend.entity.Field;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {FieldMapper.class})
public interface FarmMapper {

    @Mapping(target = "fields", ignore = true)
    Farm toEntity(CreateFarmRequest request);

    Farm updateEntity(UpdateFarmRequest updateFarmRequest, @MappingTarget Farm farm);

    @Mapping(target = "numberOfFields", expression = "java(farm.getFields().size())")
    @Mapping(target = "usedAreaInSquareMeters", expression = "java(calculateUsedArea(farm))")
    @Mapping(target = "availableAreaInSquareMeters", expression = "java(farm.getTotalAreaInSquareMeters() - calculateUsedArea(farm))")
    FarmResponse toResponse(Farm farm);

    @Named("toDetailResponse")
    @Mapping(target = "fields", source = "fields")
    @Mapping(target = "usedAreaInSquareMeters", expression = "java(calculateUsedArea(farm))")
    @Mapping(target = "availableAreaInSquareMeters", expression = "java(farm.getTotalAreaInSquareMeters() - calculateUsedArea(farm))")
    @Mapping(target = "totalTrees", expression = "java(calculateTotalTrees(farm))")
    @Mapping(target = "totalProduction", expression = "java(calculateTotalProduction(farm))")
    FarmDetailResponse toDetailResponse(Farm farm);

    default Double calculateUsedArea(Farm farm) {
        return farm.getFields().stream()
                .mapToDouble(Field::getAreaInSquareMeters)
                .sum();
    }

    default int calculateTotalTrees(Farm farm) {
        return farm.getFields().stream()
                .mapToInt(field -> field.getTrees().size())
                .sum();
    }

    default Double calculateTotalProduction(Farm farm) {
        // Implementation for calculating total production
        return 0.0; // TODO: Implement actual calculation
    }
} 