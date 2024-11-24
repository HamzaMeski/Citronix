package com.citronix.backend.controller.v1;

import com.citronix.backend.dto.farm.request.CreateFarmRequest;
import com.citronix.backend.dto.farm.request.FarmSearchCriteria;
import com.citronix.backend.dto.farm.request.UpdateFarmRequest;
import com.citronix.backend.dto.farm.response.FarmDetailResponse;
import com.citronix.backend.dto.farm.response.FarmResponse;
import com.citronix.backend.service.FarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
public class FarmController {
    private final FarmService farmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FarmResponse create(@Valid @RequestBody CreateFarmRequest request) {
        return farmService.create(request);
    }

    @PutMapping("/{id}")
    public FarmResponse update(@PathVariable Long id, 
                             @Valid @RequestBody UpdateFarmRequest request) {
        return farmService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        farmService.delete(id);
    }

    @GetMapping("/{id}")
    public FarmResponse getById(@PathVariable Long id) {
        return farmService.getById(id);
    }

    @GetMapping("/{id}/detail")
    public FarmDetailResponse getDetailById(@PathVariable Long id) {
        return farmService.getDetailById(id);
    }

    @GetMapping
    public Page<FarmResponse> getAll(Pageable pageable) {
        return farmService.getAll(pageable);
    }

    @PostMapping("/search")
    public Page<FarmResponse> search(
            @RequestBody FarmSearchCriteria criteria,
            Pageable pageable
    ) {
        return farmService.search(criteria, pageable);
    }
} 