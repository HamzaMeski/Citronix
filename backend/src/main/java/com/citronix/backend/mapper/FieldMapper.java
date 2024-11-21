package com.citronix.backend.mapper;

import com.citronix.backend.dto.field.request.CreateFieldRequest;
import com.citronix.backend.dto.field.request.UpdateFieldRequest;
import com.citronix.backend.dto.field.response.FieldDetailResponse;
import com.citronix.backend.dto.field.response.FieldResponse;
import com.citronix.backend.entity.Field;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TreeMapper.class})
public interface FieldMapper extends BaseMapper<Field, FieldResponse, CreateFieldRequest, UpdateFieldRequest> {

    @Override
    @Mapping(target = "farm", ignore = true)
    @Mapping(target = "trees", ignore = true)
    Field toEntity(CreateFieldRequest request);

    @Override
    @Mapping(target = "farmId", source = "farm.id")
    @Mapping(target = "farmName", source = "farm.name")
    @Mapping(target = "numberOfTrees", expression = "java(field.getTrees().size())")
    @Mapping(target = "maxTreeCapacity", expression = "java(calculateMaxTreeCapacity(field))")
    @Mapping(target = "occupancyRate", expression = "java(calculateOccupancyRate(field))")
    FieldResponse toDto(Field field);

    @Named("toDetailResponse")
    @Mapping(target = "farmId", source = "farm.id")
    @Mapping(target = "farmName", source = "farm.name")
    @Mapping(target = "trees", source = "trees")
    @Mapping(target = "numberOfTrees", expression = "java(field.getTrees().size())")
    @Mapping(target = "maxTreeCapacity", expression = "java(calculateMaxTreeCapacity(field))")
    @Mapping(target = "occupancyRate", expression = "java(calculateOccupancyRate(field))")
    @Mapping(target = "productivityPerHectare", expression = "java(calculateProductivityPerHectare(field))")
    FieldDetailResponse toDetailResponse(Field field);

    default int calculateMaxTreeCapacity(Field field) {
        // 100 trees per hectare
        return (int) (field.getArea() * 100);
    }

    default Double calculateOccupancyRate(Field field) {
        int maxCapacity = calculateMaxTreeCapacity(field);
        return maxCapacity > 0 ? (double) field.getTrees().size() / maxCapacity : 0.0;
    }

    default Double calculateProductivityPerHectare(Field field) {
        // TODO: Implement actual calculation based on tree productivity
        return 0.0;
    }
} 