package com.citronix.backend.mapper;

import com.citronix.backend.dto.tree.request.CreateTreeRequest;
import com.citronix.backend.dto.tree.response.TreeDetailResponse;
import com.citronix.backend.dto.tree.response.TreeHarvestResponse;
import com.citronix.backend.dto.tree.response.TreeResponse;
import com.citronix.backend.entity.HarvestDetail;
import com.citronix.backend.entity.Tree;
import com.citronix.backend.util.constants.TreeStatus;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TreeMapper extends BaseMapper<Tree, TreeResponse, CreateTreeRequest, Void> {

    @Override
    @Mapping(target = "field", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)
    @Mapping(target = "status", expression = "java(calculateInitialStatus(request.getPlantingDate()))")
    Tree toEntity(CreateTreeRequest request);

    @Override
    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.farm.name")
    @Mapping(target = "ageInYears", expression = "java(calculateAge(tree.getPlantingDate()))")
    @Mapping(target = "expectedProductivityPerSeason", expression = "java(calculateExpectedProductivity(tree))")
    TreeResponse toDto(Tree tree);

    @Named("toDetailResponse")
    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.farm.name")
    @Mapping(target = "ageInYears", expression = "java(calculateAge(tree.getPlantingDate()))")
    @Mapping(target = "expectedProductivityPerSeason", expression = "java(calculateExpectedProductivity(tree))")
    @Mapping(target = "totalProduction", expression = "java(calculateTotalProduction(tree))")
    @Mapping(target = "averageProductionPerSeason", expression = "java(calculateAverageProduction(tree))")
    TreeDetailResponse toDetailResponse(Tree tree);

    default TreeStatus calculateInitialStatus(LocalDate plantingDate) {
        return TreeStatus.YOUNG;
    }

    default int calculateAge(LocalDate plantingDate) {
        return Period.between(plantingDate, LocalDate.now()).getYears();
    }

    default double calculateExpectedProductivity(Tree tree) {
        int age = calculateAge(tree.getPlantingDate());
        if (age < 3) return 2.5;
        if (age <= 10) return 12.0;
        if (age <= 20) return 20.0;
        return 0.0;
    }

    default double calculateTotalProduction(Tree tree) {
        return tree.getHarvestDetails().stream()
                .mapToDouble(harvestDetail -> harvestDetail.getQuantity())
                .sum();
    }

    default double calculateAverageProduction(Tree tree) {
        long totalSeasons = tree.getHarvestDetails().stream()
                .map(harvestDetail -> harvestDetail.getHarvest().getSeason())
                .distinct()
                .count();

        double totalProduction = calculateTotalProduction(tree);

        return totalSeasons > 0 ? totalProduction / totalSeasons : 0.0;
    }

    @Mapping(target = "harvestId", source = "harvest.id")
    @Mapping(target = "harvestDate", source = "harvest.harvestDate")
    @Mapping(target = "season", source = "harvest.season")
    @Mapping(target = "quantity", source = "quantity")
    TreeHarvestResponse toHarvestResponse(HarvestDetail harvestDetail);

    default List<TreeHarvestResponse> mapHarvestHistory(List<HarvestDetail> harvestDetails) {
        if (harvestDetails == null) return new ArrayList<>();
        return harvestDetails.stream()
                .map(this::toHarvestResponse)
                .sorted(Comparator.comparing(TreeHarvestResponse::getHarvestDate).reversed())
                .collect(Collectors.toList());
    }

    @AfterMapping
    default void setHarvestHistory(@MappingTarget TreeDetailResponse response, Tree tree) {
        response.setHarvestHistory(mapHarvestHistory(tree.getHarvestDetails()));
    }
}