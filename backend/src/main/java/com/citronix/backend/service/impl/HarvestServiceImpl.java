package com.citronix.backend.service.impl;

import com.citronix.backend.dto.harvest.request.CreateHarvestRequest;
import com.citronix.backend.dto.harvest.request.AddHarvestDetailRequest;
import com.citronix.backend.dto.harvest.response.HarvestResponse;
import com.citronix.backend.dto.harvest.response.HarvestDetailResponse;
import com.citronix.backend.entity.Harvest;
import com.citronix.backend.entity.HarvestDetail;
import com.citronix.backend.entity.Tree;
import com.citronix.backend.exception.ResourceNotFoundException;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.HarvestMapper;
import com.citronix.backend.repository.HarvestRepository;
import com.citronix.backend.repository.TreeRepository;
import com.citronix.backend.service.HarvestService;
import com.citronix.backend.util.constants.Season;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HarvestServiceImpl implements HarvestService {
    private final HarvestRepository harvestRepository;
    private final TreeRepository treeRepository;
    private final HarvestMapper harvestMapper;

    @Override
    @Transactional
    public HarvestResponse create(CreateHarvestRequest request) {
        validateHarvestCreation(request);
        
        Harvest harvest = harvestMapper.toEntity(request);
        return harvestMapper.toResponse(harvestRepository.save(harvest));
    }

    @Override
    @Transactional
    public void addHarvestDetail(AddHarvestDetailRequest request) {
        Harvest harvest = harvestRepository.findById(request.getHarvestId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));

        Tree tree = treeRepository.findById(request.getTreeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));

        validateHarvestDetail(harvest, tree, request);

        HarvestDetail detail = HarvestDetail.builder()
                .harvest(harvest)
                .tree(tree)
                .quantity(request.getQuantity())
                .build();

        harvest.getHarvestDetails().add(detail);
        harvestRepository.save(harvest);
    }

    @Override
    public HarvestResponse getById(Long id) {
        return harvestRepository.findById(id)
                .map(harvestMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
    }

    @Override
    public HarvestDetailResponse getDetailById(Long id) {
        return harvestRepository.findById(id)
                .map(harvestMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
    }

    @Override
    public Page<HarvestResponse> getAll(Pageable pageable) {
        return harvestRepository.findAll(pageable)
                .map(harvestMapper::toResponse);
    }

    @Override
    public Page<HarvestResponse> getAllHarvestsBySeason(Season season, Pageable pageable) {
        return harvestRepository.findAllBySeason(season, pageable)
                .map(harvestMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));

        if (!harvest.getSales().isEmpty()) {
            throw new ValidationException("Cannot delete harvest with existing sales");
        }

        harvestRepository.deleteById(id);
    }

    private void validateHarvestCreation(CreateHarvestRequest request) {
        // Validate season matches harvest date
        Season calculatedSeason = calculateSeason(request.getHarvestDate());
        if (request.getSeason() != calculatedSeason) {
            throw new ValidationException("Season does not match harvest date");
        }

        // Check if harvest already exists for this season
        if (harvestRepository.existsBySeasonAndHarvestDateBetween(
                request.getSeason(),
                request.getHarvestDate().withDayOfYear(1),
                request.getHarvestDate().withDayOfYear(365))) {
            throw new ValidationException("A harvest already exists for this season");
        }
    }

    private void validateHarvestDetail(Harvest harvest, Tree tree, AddHarvestDetailRequest request) {
        // Check if tree has already been harvested this season
        if (tree.getHarvestDetails().stream()
                .anyMatch(detail ->
                        detail.getHarvest().getSeason() == harvest.getSeason()
                        && detail.getHarvest().getHarvestDate() == harvest.getHarvestDate()
                )) {
            throw new ValidationException("Tree has already been harvested this season");
        }

        // Validate tree productivity based on status
        double expectedProductivity = calculateExpectedProductivity(tree);
        if (request.getQuantity() > expectedProductivity * 1.2) { // 20% tolerance
            throw new ValidationException("Harvest quantity exceeds expected productivity for tree's age");
        }
    }

    private Season calculateSeason(LocalDate date) {
        int month = date.getMonthValue();
        return switch (month) {
            case 12, 1, 2 -> Season.WINTER;
            case 3, 4, 5 -> Season.SPRING;
            case 6, 7, 8 -> Season.SUMMER;
            case 9, 10, 11 -> Season.AUTUMN;
            default -> throw new ValidationException("Invalid month: " + month);
        };
    }

    private double calculateExpectedProductivity(Tree tree) {
        return switch (tree.getStatus()) {
            case YOUNG -> 2.5;
            case MATURE -> 12.0;
            case OLD -> 20.0;
            case INACTIVE -> 0.0;
        };
    }
} 