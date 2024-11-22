package com.citronix.backend.service.impl;

import com.citronix.backend.dto.tree.request.CreateTreeRequest;
import com.citronix.backend.dto.tree.response.TreeDetailResponse;
import com.citronix.backend.dto.tree.response.TreeResponse;
import com.citronix.backend.entity.Field;
import com.citronix.backend.entity.Tree;
import com.citronix.backend.exception.ResourceNotFoundException;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.TreeMapper;
import com.citronix.backend.repository.FieldRepository;
import com.citronix.backend.repository.TreeRepository;
import com.citronix.backend.service.TreeService;
import com.citronix.backend.util.constants.TreeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreeServiceImpl implements TreeService {
    private final TreeRepository treeRepository;
    private final FieldRepository fieldRepository;
    private final TreeMapper treeMapper;

    @Override
    @Transactional
    public TreeResponse create(CreateTreeRequest request) {
        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));

        validateTreeCreation(field, request);

        Tree tree = treeMapper.toEntity(request);
        tree.setField(field);
        tree.setStatus(calculateTreeStatus(request.getPlantingDate()));

        return treeMapper.toDto(treeRepository.save(tree));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));

        if (!tree.getHarvestDetails().isEmpty()) {
            throw new ValidationException("Cannot delete tree with existing harvest records");
        }

        treeRepository.deleteById(id);
    }

    @Override
    public TreeDetailResponse findById(Long id) {
        return treeRepository.findById(id)
                .map(treeMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));
    }

    @Override
    public List<TreeResponse> findAllByFieldId(Long fieldId) {
        if (!fieldRepository.existsById(fieldId)) {
            throw new ResourceNotFoundException("Field not found");
        }
        return treeRepository.findAllByFieldId(fieldId).stream()
                .map(treeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateTreeStatus(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));

        TreeStatus newStatus = calculateTreeStatus(tree.getPlantingDate());
        if (tree.getStatus() != newStatus) {
            tree.setStatus(newStatus);
            treeRepository.save(tree);
        }
    }

    private void validateTreeCreation(Field field, CreateTreeRequest request) {
        // Check if field has reached maximum tree capacity
        int currentTrees = field.getTrees().size();
        int maxCapacity = calculateMaxTreeCapacity(field.getAreaInSquareMeters());
        
        if (currentTrees >= maxCapacity) {
            throw new ValidationException(
                    String.format("Field has reached maximum tree capacity of %d trees", maxCapacity));
        }

        // Validate planting season (March to May)
        int plantingMonth = request.getPlantingDate().getMonthValue();
        if (plantingMonth < 3 || plantingMonth > 5) {
            throw new ValidationException("Trees can only be planted between March and May");
        }
    }

    private int calculateMaxTreeCapacity(Double areaInSquareMeters) {
        // 100 trees per hectare (10,000 m²)
        return (int) (areaInSquareMeters / 100);
    }

    private TreeStatus calculateTreeStatus(LocalDate plantingDate) {
        int ageInYears = Period.between(plantingDate, LocalDate.now()).getYears();
        
        if (ageInYears >= 20) {
            return TreeStatus.INACTIVE;
        } else if (ageInYears > 10) {
            return TreeStatus.OLD;
        } else if (ageInYears >= 3) {
            return TreeStatus.MATURE;
        } else {
            return TreeStatus.YOUNG;
        }
    }
} 