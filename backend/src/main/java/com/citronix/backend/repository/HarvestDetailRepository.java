package com.citronix.backend.repository;

import com.citronix.backend.entity.HarvestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {
} 