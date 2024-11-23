package com.citronix.backend.service.impl;

import com.citronix.backend.dto.field.request.CreateFieldRequest;
import com.citronix.backend.dto.field.request.UpdateFieldRequest;
import com.citronix.backend.dto.field.response.FieldDetailResponse;
import com.citronix.backend.dto.field.response.FieldResponse;
import com.citronix.backend.entity.Farm;
import com.citronix.backend.entity.Field;
import com.citronix.backend.exception.ResourceNotFoundException;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.FieldMapper;
import com.citronix.backend.repository.FarmRepository;
import com.citronix.backend.repository.FieldRepository;
import com.citronix.backend.service.FieldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;
    private final FieldMapper fieldMapper;

    private static final int MAX_FIELDS_PER_FARM = 10;
    private static final double MIN_FIELD_AREA = 1000.0; // 1 hectare in m²
    private static final double MAX_FIELD_AREA_PERCENTAGE = 0.5; // 50% of farm area

    @Override
    @Transactional
    public FieldResponse create(CreateFieldRequest request) {
        Farm farm = farmRepository.findById(request.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found"));

        validateFieldCreation(farm, request.getAreaInSquareMeters());

        Field field = fieldMapper.toEntity(request);
        field.setFarm(farm);

        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Override
    @Transactional
    public FieldResponse update(Long id, UpdateFieldRequest request) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));

        Farm farm = field.getFarm();
        Double currentUsedArea = calculateUsedArea(farm) - field.getAreaInSquareMeters();

        validateFieldUpdate(farm, request.getAreaInSquareMeters(), currentUsedArea, field);

        field.setAreaInSquareMeters(request.getAreaInSquareMeters());
        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));

        if (!field.getTrees().isEmpty()) {
            throw new IllegalStateException("Cannot delete field with existing trees");
        }

        fieldRepository.deleteById(id);
    }

    @Override
    public FieldDetailResponse findById(Long id) {
        return fieldRepository.findById(id)
                .map(fieldMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));
    }

    @Override
    public List<FieldResponse> findAllByFarmId(Long farmId) {
        if (!farmRepository.existsById(farmId)) {
            throw new ResourceNotFoundException("Farm not found");
        }
        return fieldRepository.findAllByFarmId(farmId).stream()
                .map(fieldMapper::toResponse)
                .toList();
    }

    private void validateFieldCreation(Farm farm, Double fieldArea) {
        // Validate maximum number of fields
        if (farm.getFields().size() >= MAX_FIELDS_PER_FARM) {
            throw new ValidationException(
                    String.format("Farm cannot have more than %d fields", MAX_FIELDS_PER_FARM)
            );
        }

        validateFieldArea(farm, fieldArea, calculateUsedArea(farm));
    }

    private void validateFieldUpdate(Farm farm, Double newFieldArea, Double currentUsedArea, Field existingField) {
        // Check if the field has trees when reducing area
        if (newFieldArea < existingField.getAreaInSquareMeters() && !existingField.getTrees().isEmpty()) {
            int currentTrees = existingField.getTrees().size();
            int newMaxCapacity = (int) (newFieldArea / 100.0); // 100 trees per hectare
            if (currentTrees > newMaxCapacity) {
                throw new ValidationException(
                        String.format("Cannot reduce field area: current tree count (%d) exceeds new maximum capacity (%d)",
                                currentTrees, newMaxCapacity)
                );
            }
        }

        validateFieldArea(farm, newFieldArea, currentUsedArea);
    }

    private void validateFieldArea(Farm farm, Double fieldArea, Double currentUsedArea) {
        // Validate maximum area (50% of farm)
        double maxFieldArea = farm.getTotalAreaInSquareMeters() * MAX_FIELD_AREA_PERCENTAGE;
        if (fieldArea > maxFieldArea) {
            throw new ValidationException(
                    String.format("Field area (%.2f m²) exceeds maximum allowed area (%.2f m²)",
                            fieldArea, maxFieldArea)
            );
        }

        // Validate available area
        if (fieldArea > farm.getTotalAreaInSquareMeters() - currentUsedArea) {
            throw new ValidationException(
                    String.format("Field area (%.2f m²) exceeds available farm area (%.2f m²)",
                            fieldArea, farm.getTotalAreaInSquareMeters() - currentUsedArea)
            );
        }
    }

    private Double calculateUsedArea(Farm farm) {
        return farm.getFields().stream()
                .mapToDouble(Field::getAreaInSquareMeters)
                .sum();
    }
}