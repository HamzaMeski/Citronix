package com.citronix.backend.service.impl;

import com.citronix.backend.dto.farm.request.CreateFarmRequest;
import com.citronix.backend.dto.farm.request.UpdateFarmRequest;
import com.citronix.backend.dto.farm.response.FarmDetailResponse;
import com.citronix.backend.dto.farm.response.FarmResponse;
import com.citronix.backend.entity.Farm;
import com.citronix.backend.exception.ResourceNotFoundException;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.FarmMapper;
import com.citronix.backend.repository.FarmRepository;
import com.citronix.backend.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {
    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;

    @Override
    @Transactional
    public FarmResponse create(CreateFarmRequest request) {
        validateCreate(request);
        Farm farm = farmMapper.toEntity(request);
        return farmMapper.toDto(farmRepository.save(farm));
    }

    @Override
    @Transactional
    public FarmResponse update(Long id, UpdateFarmRequest request) {
        Farm farm = getFarmById(id);
        validateUpdate(farm, request);

        farmMapper.updateEntity(request, farm);
        return farmMapper.toDto(farmRepository.save(farm));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Farm farm = getFarmById(id);
        validateDelete(farm);
        farmRepository.delete(farm);
    }

    @Override
    @Transactional(readOnly = true)
    public FarmResponse getById(Long id) {
        return farmMapper.toDto(getFarmById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public FarmDetailResponse getDetailById(Long id) {
        return farmMapper.toDetailResponse(getFarmById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FarmResponse> getAll(Pageable pageable) {
        return farmRepository.findAll(pageable)
                .map(farmMapper::toDto);
    }

    private Farm getFarmById(Long id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
    }


    // validtion functions
    private void validateCreate(CreateFarmRequest request) {
        if (farmRepository.existsByName(request.getName())) {
            throw new ValidationException("Farm with name " + request.getName() + " already exists");
        }
    }

    private void validateUpdate(Farm farm, UpdateFarmRequest request) {
        double usedArea = farm.getFields().stream()
                .mapToDouble(field -> field.getAreaInSquareMeters())
                .sum();

        if (request.getTotalAreaInSquareMeters() < usedArea) {
            throw new ValidationException(
                    "New farm area cannot be smaller than sum of existing fields: " + usedArea + " m²");
        }

        if (!request.getName().equals(farm.getName()) &&
                farmRepository.existsByName(request.getName())) {
            throw new ValidationException("Farm with name " + request.getName() + " already exists");
        }
    }

    private void validateDelete(Farm farm) {
        if (!farm.getFields().isEmpty()) {
            throw new ValidationException("Cannot delete farm that has fields");
        }
    }
}