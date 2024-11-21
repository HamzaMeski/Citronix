package com.citronix.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlantingSeasonValidator.class)
@Documented
public @interface PlantingSeasonValid {
    String message() default "Trees can only be planted between March and May";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 