package com.citronix.backend.service;

import com.citronix.backend.dto.harvest.request.CreateHarvestRequest;
import com.citronix.backend.dto.harvest.request.AddHarvestDetailRequest;
import com.citronix.backend.dto.harvest.response.HarvestResponse;
import com.citronix.backend.dto.harvest.response.HarvestDetailResponse;
import com.citronix.backend.util.constants.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HarvestService {
    HarvestResponse create(CreateHarvestRequest request);
    void addHarvestDetail(AddHarvestDetailRequest request);
    HarvestResponse getById(Long id);
    HarvestDetailResponse getDetailById(Long id);
    Page<HarvestResponse> getAll(Pageable pageable);
    Page<HarvestResponse> getAllHarvestsBySeason(Season season, Pageable pageable);
    void delete(Long id);
} 