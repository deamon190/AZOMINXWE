package com.classroom.azominxwe.validation;

import com.classroom.azominxwe.model.Trimestre;
import com.classroom.azominxwe.repository.TrimestreRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueActiveTrimestreValidator implements ConstraintValidator<UniqueActiveTrimestre, Trimestre> {

    private final TrimestreRepository trimestreRepository;

    @Autowired
    public UniqueActiveTrimestreValidator(TrimestreRepository trimestreRepository) {
        this.trimestreRepository = trimestreRepository;
    }

    @Override
    public boolean isValid(Trimestre trimestre, ConstraintValidatorContext context) {
        if (trimestre.getActif() == null || !trimestre.getActif()) {
            return true; // No need to validate if the trimestre is not active
        }
        Long count = trimestreRepository.countByActif(true);
        return count == 0 || (count == 1 && trimestreRepository.findById(trimestre.getTrimestreId()).isPresent());
    }
}
