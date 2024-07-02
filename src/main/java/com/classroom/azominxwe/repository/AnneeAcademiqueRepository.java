package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.AnneeAcademique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique, Long> {

    // Méthodes de recherche personnalisées peuvent être ajoutées ici
    AnneeAcademique findByActifTrue();
}
