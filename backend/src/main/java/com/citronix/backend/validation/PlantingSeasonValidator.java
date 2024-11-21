package com.citronix.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class PlantingSeasonValidator implements ConstraintValidator<PlantingSeasonValid, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Let @NotNull handle null validation
        }
        Month month = date.getMonth();
        return month == Month.MARCH || month == Month.APRIL || month == Month.MAY;
    }
} 