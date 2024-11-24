package com.citronix.backend.service;

import com.citronix.backend.dto.farm.request.CreateFarmRequest;
import com.citronix.backend.dto.farm.request.FarmSearchCriteria;
import com.citronix.backend.dto.farm.request.UpdateFarmRequest;
import com.citronix.backend.dto.farm.response.FarmDetailResponse;
import com.citronix.backend.dto.farm.response.FarmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FarmService {
    FarmResponse create(CreateFarmRequest request);
    FarmResponse update(Long id, UpdateFarmRequest request);
    void delete(Long id);
    FarmResponse getById(Long id);
    FarmDetailResponse getDetailById(Long id);
    Page<FarmResponse> getAll(Pageable pageable);
    Page<FarmResponse> search(FarmSearchCriteria criteria, Pageable pageable);
} 