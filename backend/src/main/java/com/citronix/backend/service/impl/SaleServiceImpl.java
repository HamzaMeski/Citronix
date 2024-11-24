package com.citronix.backend.service.impl;

import com.citronix.backend.dto.sale.request.CreateSaleRequest;
import com.citronix.backend.dto.sale.response.SaleResponse;
import com.citronix.backend.entity.Harvest;
import com.citronix.backend.entity.Sale;
import com.citronix.backend.exception.ResourceNotFoundException;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.SaleMapper;
import com.citronix.backend.repository.HarvestRepository;
import com.citronix.backend.repository.SaleRepository;
import com.citronix.backend.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final HarvestRepository harvestRepository;
    private final SaleMapper saleMapper;

    @Override
    @Transactional
    public SaleResponse create(CreateSaleRequest request) {
        Harvest harvest = harvestRepository.findById(request.getHarvestId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));

        validateSaleCreation(harvest, request);

        Sale sale = saleMapper.toEntity(request);
        sale.setHarvest(harvest);

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    public SaleResponse getById(Long id) {
        return saleRepository.findById(id)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    @Override
    public Page<SaleResponse> getAll(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(saleMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sale not found");
        }
        saleRepository.deleteById(id);
    }

    private void validateSaleCreation(Harvest harvest, CreateSaleRequest request) {
        // Calculate total sold quantity including this sale
        double totalSoldQuantity = harvest.getSales().stream()
                .mapToDouble(Sale::getQuantity)
                .sum() + request.getQuantity();

        // Ensure we don't sell more than harvested
        if (totalSoldQuantity > harvest.getTotalQuantity()) {
            throw new ValidationException(
                    String.format("Cannot sell more than harvested quantity. Available: %.2f kg, Requested: %.2f kg",
                            harvest.getTotalQuantity() - harvest.getSales().stream()
                                    .mapToDouble(Sale::getQuantity)
                                    .sum(),
                            request.getQuantity()));
        }

        // Validate sale date is after harvest date
        if (request.getSaleDate().isBefore(harvest.getHarvestDate())) {
            throw new ValidationException("Sale date cannot be before harvest date");
        }
    }
} 