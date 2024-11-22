package com.citronix.backend.service;

import com.citronix.backend.dto.field.request.CreateFieldRequest;
import com.citronix.backend.dto.field.request.UpdateFieldRequest;
import com.citronix.backend.dto.field.response.FieldDetailResponse;
import com.citronix.backend.dto.field.response.FieldResponse;
import java.util.List;

public interface FieldService {
    FieldResponse create(CreateFieldRequest request);
    FieldResponse update(Long id, UpdateFieldRequest request);
    void delete(Long id);
    FieldDetailResponse findById(Long id);
    List<FieldResponse> findAllByFarmId(Long farmId);
}