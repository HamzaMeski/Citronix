package com.citronix.backend.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, D, C, U> {
    E toEntity(C createDto);
    D toDto(E entity);
    void updateEntity(U updateDto, @MappingTarget E entity);
} 