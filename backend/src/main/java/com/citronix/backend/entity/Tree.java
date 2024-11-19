package com.citronix.backend.entity;

import com.citronix.backend.util.constants.TreeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Planting date is required")
    private LocalDate plantingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @OneToMany(mappedBy = "tree", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tree status is required")
    private TreeStatus status;
} 