package com.classroom.azominxwe.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.classroom.azominxwe.model.Trimestre;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Trimestre> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(Trimestre trimestre, ConstraintValidatorContext context) {
        if (trimestre.getDateDebut() == null || trimestre.getDateFin() == null) {
            return true; // Validation for nulls should be handled by @NotNull
        }
        if (!trimestre.getDateDebut().before(trimestre.getDateFin())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La date de début doit être antérieure à la date de fin")
                    .addPropertyNode("dateDebut")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
