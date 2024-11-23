package com.citronix.backend.repository;

import com.citronix.backend.entity.Harvest;
import com.citronix.backend.util.constants.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    boolean existsBySeasonAndHarvestDateBetween(Season season, LocalDate startDate, LocalDate endDate);
    Page<Harvest> findAllBySeason(Season season, Pageable pageable);
}