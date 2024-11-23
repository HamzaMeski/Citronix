package com.citronix.backend.service;

import com.citronix.backend.dto.sale.request.CreateSaleRequest;
import com.citronix.backend.dto.sale.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {
    SaleResponse create(CreateSaleRequest request);
    SaleResponse getById(Long id);
    Page<SaleResponse> getAll(Pageable pageable);
    void delete(Long id);
} 