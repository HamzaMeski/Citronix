package com.citronix.backend.mapper;

import com.citronix.backend.dto.tree.request.CreateTreeRequest;
import com.citronix.backend.dto.tree.response.TreeDetailResponse;
import com.citronix.backend.dto.tree.response.TreeHarvestResponse;
import com.citronix.backend.dto.tree.response.TreeResponse;
import com.citronix.backend.entity.HarvestDetail;
import com.citronix.backend.entity.Tree;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.util.constants.TreeStatus;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TreeMapper extends BaseMapper<Tree, TreeResponse, CreateTreeRequest, Void> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "field", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)
    @Mapping(target = "status", ignore = true)
    Tree toEntity(CreateTreeRequest request);

    @Override
    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.farm.name")
    @Mapping(target = "ageInYears", expression = "java(calculateAge(tree.getPlantingDate()))")
    @Mapping(target = "expectedProductivityPerSeason", expression = "java(calculateExpectedProductivity(tree.getStatus()))")
    TreeResponse toDto(Tree tree);

    @Named("toDetailResponse")
    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.farm.name")
    @Mapping(target = "ageInYears", expression = "java(calculateAge(tree.getPlantingDate()))")
    @Mapping(target = "expectedProductivityPerSeason", expression = "java(calculateExpectedProductivity(tree.getStatus()))")
    @Mapping(target = "totalProduction", expression = "java(calculateTotalProduction(tree))")
    @Mapping(target = "averageProductionPerSeason", expression = "java(calculateAverageProductionPerSeason(tree))")
    @Mapping(target = "harvestHistory", expression = "java(mapHarvestHistory(tree))")
    TreeDetailResponse toDetailResponse(Tree tree);

    default int calculateAge(LocalDate plantingDate) {
        return Period.between(plantingDate, LocalDate.now()).getYears();
    }

    default double calculateExpectedProductivity(TreeStatus status) {
        switch (status) {
            case YOUNG:
                return 2.5; // < 3 years
            case MATURE:
                return 12.0; // 3-calculateAverageProductionPerSeason10 years
            case OLD:
                return 20.0; // 10-20 years
            case INACTIVE:
                return 0.0; // > 20 years
            default:
                throw new ValidationException("Unknown TreeStatus: " + status);
        }
    }

    default double calculateTotalProduction(Tree tree) {
        return tree.getHarvestDetails().stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
    }

    default double calculateAverageProductionPerSeason(Tree tree) {
        if (tree.getHarvestDetails().isEmpty()) {
            return 0.0;
        }
        return calculateTotalProduction(tree) / tree.getHarvestDetails().size();
    }

    default List<TreeHarvestResponse> mapHarvestHistory(Tree tree) {
        return tree.getHarvestDetails().stream()
                .map(detail -> TreeHarvestResponse.builder()
                        .harvestId(detail.getHarvest().getId())
                        .harvestDate(detail.getHarvest().getHarvestDate())
                        .season(detail.getHarvest().getSeason())
                        .quantity(detail.getQuantity())
                        .build())
                .sorted(Comparator.comparing(TreeHarvestResponse::getHarvestDate).reversed())
                .collect(Collectors.toList());
    }
}