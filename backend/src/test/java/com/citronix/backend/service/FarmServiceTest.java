package com.citronix.backend.service;

import com.citronix.backend.dto.farm.request.CreateFarmRequest;
import com.citronix.backend.dto.farm.response.FarmResponse;
import com.citronix.backend.entity.Farm;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.FarmMapper;
import com.citronix.backend.repository.FarmRepository;
import com.citronix.backend.service.impl.FarmServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    @InjectMocks
    private FarmServiceImpl farmService;

    private CreateFarmRequest validRequest;
    private Farm validFarm;
    private FarmResponse validResponse;

    @BeforeEach
    void setUp() {
        validRequest = CreateFarmRequest.builder()
                .name("Test Farm")
                .location("Test Location")
                .totalAreaInSquareMeters(10000.0)
                .build();

        validFarm = Farm.builder()
                .id(1L)
                .name("Test Farm")
                .location("Test Location")
                .totalAreaInSquareMeters(10000.0)
                .creationDate(LocalDate.now())
                .build();

        validResponse = FarmResponse.builder()
                .id(1L)
                .name("Test Farm")
                .location("Test Location")
                .totalAreaInSquareMeters(10000.0)
                .creationDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Should create farm successfully")
    void createFarm_WithValidData_ShouldSucceed() {
        // Arrange
        when(farmRepository.existsByName(validRequest.getName())).thenReturn(false);
        when(farmMapper.toEntity(validRequest)).thenReturn(validFarm);
        when(farmRepository.save(any(Farm.class))).thenReturn(validFarm);
        when(farmMapper.toResponse(validFarm)).thenReturn(validResponse);

        // Act
        FarmResponse response = farmService.create(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(validResponse.getId(), response.getId());
        assertEquals(validResponse.getName(), response.getName());
        assertEquals(validResponse.getLocation(), response.getLocation());
        assertEquals(validResponse.getTotalAreaInSquareMeters(), response.getTotalAreaInSquareMeters());

        // Verify interactions
        verify(farmRepository).existsByName(validRequest.getName());
        verify(farmMapper).toEntity(validRequest);
        verify(farmRepository).save(any(Farm.class));
        verify(farmMapper).toResponse(validFarm);
    }

    @Test
    @DisplayName("Should throw exception when farm name already exists")
    void createFarm_WithDuplicateName_ShouldThrowException() {
        // Arrange
        when(farmRepository.existsByName(validRequest.getName())).thenReturn(true);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> farmService.create(validRequest));
        assertEquals("Farm with name Test Farm already exists", exception.getMessage());

        // Verify interactions
        verify(farmRepository).existsByName(validRequest.getName());
        verify(farmMapper, never()).toEntity(any());
        verify(farmRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when farm name is null")
    void createFarm_WithNullName_ShouldThrowException() {
        // Arrange
        CreateFarmRequest invalidRequest = CreateFarmRequest.builder()
                .location("Test Location")
                .totalAreaInSquareMeters(10000.0)
                .build();

        when(farmMapper.toEntity(invalidRequest)).thenReturn(null); // This simulates the actual behavior

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> farmService.create(invalidRequest));
        assertTrue(exception.getMessage().contains("Cannot invoke"));

        // Verify interactions
        verify(farmMapper).toEntity(invalidRequest);
        verify(farmRepository, never()).save(any());
    }
}