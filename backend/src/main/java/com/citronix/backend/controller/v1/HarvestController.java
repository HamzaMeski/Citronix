package com.citronix.backend.controller.v1;

import com.citronix.backend.dto.harvest.request.CreateHarvestRequest;
import com.citronix.backend.dto.harvest.request.AddHarvestDetailRequest;
import com.citronix.backend.dto.harvest.response.HarvestResponse;
import com.citronix.backend.dto.harvest.response.HarvestDetailResponse;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.service.HarvestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
public class HarvestController {
    private final HarvestService harvestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HarvestResponse create(@Valid @RequestBody CreateHarvestRequest request) {
        return harvestService.create(request);
    }

    @PostMapping("/details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addHarvestDetail(
            @Valid @RequestBody AddHarvestDetailRequest request
    ) {
        harvestService.addHarvestDetail(request);
    }

    @GetMapping("/{id}")
    public HarvestResponse getById(@PathVariable Long id) {
        return harvestService.getById(id);
    }

    @GetMapping("/{id}/details")
    public HarvestDetailResponse getDetailById(@PathVariable Long id) {
        return harvestService.getDetailById(id);
    }

    @GetMapping
    public Page<HarvestResponse> getAll(Pageable pageable) {
        return harvestService.getAll(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        harvestService.delete(id);
    }
} 