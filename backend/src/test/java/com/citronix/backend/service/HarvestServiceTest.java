package com.citronix.backend.service;

import com.citronix.backend.dto.harvest.request.CreateHarvestRequest;
import com.citronix.backend.dto.harvest.response.HarvestResponse;
import com.citronix.backend.entity.Harvest;
import com.citronix.backend.exception.ValidationException;
import com.citronix.backend.mapper.HarvestMapper;
import com.citronix.backend.repository.HarvestRepository;
import com.citronix.backend.service.impl.HarvestServiceImpl;
import com.citronix.backend.util.constants.Season;
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
class HarvestServiceTest {

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private HarvestMapper harvestMapper;

    @InjectMocks
    private HarvestServiceImpl harvestService;

    private LocalDate validHarvestDate;
    private CreateHarvestRequest validRequest;
    private Harvest validHarvest;
    private HarvestResponse validResponse;

    @BeforeEach
    void setUp() {
        validHarvestDate = LocalDate.of(2024, 3, 15); // Spring date

        validRequest = CreateHarvestRequest.builder()
                .harvestDate(validHarvestDate)
                .season(Season.SPRING)
                .build();

        validHarvest = Harvest.builder()
                .id(1L)
                .harvestDate(validHarvestDate)
                .season(Season.SPRING)
                .build();

        validResponse = HarvestResponse.builder()
                .id(1L)
                .harvestDate(validHarvestDate)
                .season(Season.SPRING)
                .totalQuantity(0.0)
                .numberOfTrees(0)
                .averageQuantityPerTree(0.0)
                .availableQuantity(0.0)
                .build();
    }

    @Test
    @DisplayName("Should create harvest successfully")
    void createHarvest_WithValidData_ShouldSucceed() {
        // Arrange
        when(harvestMapper.toEntity(validRequest)).thenReturn(validHarvest);
        when(harvestRepository.save(any(Harvest.class))).thenReturn(validHarvest);
        when(harvestMapper.toResponse(validHarvest)).thenReturn(validResponse);

        // Act
        HarvestResponse response = harvestService.create(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(validResponse.getId(), response.getId());
        assertEquals(validResponse.getSeason(), response.getSeason());
        assertEquals(validResponse.getHarvestDate(), response.getHarvestDate());

        // Verify interactions
        verify(harvestMapper).toEntity(validRequest);
        verify(harvestRepository).save(any(Harvest.class));
        verify(harvestMapper).toResponse(validHarvest);
    }

    @Test
    @DisplayName("Should throw exception when season doesn't match date")
    void createHarvest_WithInvalidSeason_ShouldThrowException() {
        // Arrange
        CreateHarvestRequest invalidRequest = CreateHarvestRequest.builder()
                .harvestDate(LocalDate.of(2024, 3, 15)) // Spring date
                .season(Season.SUMMER) // Wrong season
                .build();

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> harvestService.create(invalidRequest));
        assertEquals("Season does not match harvest date", exception.getMessage());

        // Verify no interactions with repository or mapper
        verify(harvestMapper, never()).toEntity(any());
        verify(harvestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when date is null")
    void createHarvest_WithNullDate_ShouldThrowException() {
        // Arrange
        CreateHarvestRequest invalidRequest = CreateHarvestRequest.builder()
                .season(Season.SPRING)
                .build();

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class,
                () -> harvestService.create(invalidRequest));
        assertTrue(exception.getMessage().contains("null"));

        // Verify no interactions
        verify(harvestMapper, never()).toEntity(any());
        verify(harvestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when season is null")
    void createHarvest_WithNullSeason_ShouldThrowException() {
        // Arrange
        CreateHarvestRequest invalidRequest = CreateHarvestRequest.builder()
                .harvestDate(validHarvestDate)
                .season(null)
                .build();

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> harvestService.create(invalidRequest));
        assertEquals("Season does not match harvest date", exception.getMessage());

        // Verify no interactions with repository
        verify(harvestRepository, never()).save(any());
    }
}