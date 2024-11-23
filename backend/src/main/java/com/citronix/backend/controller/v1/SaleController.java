package com.citronix.backend.controller.v1;

import com.citronix.backend.dto.sale.request.CreateSaleRequest;
import com.citronix.backend.dto.sale.response.SaleResponse;
import com.citronix.backend.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponse create(@Valid @RequestBody CreateSaleRequest request) {
        return saleService.create(request);
    }

    @GetMapping("/{id}")
    public SaleResponse getById(@PathVariable Long id) {
        return saleService.getById(id);
    }

    @GetMapping
    public Page<SaleResponse> getAll(Pageable pageable) {
        return saleService.getAll(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        saleService.delete(id);
    }
} 