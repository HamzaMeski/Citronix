package com.citronix.backend.controller.v1;

import com.citronix.backend.dto.field.request.CreateFieldRequest;
import com.citronix.backend.dto.field.request.UpdateFieldRequest;
import com.citronix.backend.dto.field.response.FieldDetailResponse;
import com.citronix.backend.dto.field.response.FieldResponse;
import com.citronix.backend.service.FieldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FieldResponse create(@Valid @RequestBody CreateFieldRequest request) {
        return fieldService.create(request);
    }

    @PutMapping("/{id}")
    public FieldResponse update(@PathVariable Long id, @Valid @RequestBody UpdateFieldRequest request) {
        return fieldService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        fieldService.delete(id);
    }

    @GetMapping("/{id}")
    public FieldDetailResponse findById(@PathVariable Long id) {
        return fieldService.findById(id);
    }

    @GetMapping("/farm/{farmId}")
    public List<FieldResponse> findAllByFarmId(@PathVariable Long farmId) {
        return fieldService.findAllByFarmId(farmId);
    }
} 