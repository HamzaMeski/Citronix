package com.citronix.backend.entity;

import com.citronix.backend.util.constants.Season;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Season is required")
    private Season season;

    @NotNull(message = "Total quantity is required")
    @Positive(message = "Total quantity must be positive")
    private Double totalQuantity;

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sale> sales = new ArrayList<>();
} 