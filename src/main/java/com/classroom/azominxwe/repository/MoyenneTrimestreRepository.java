package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MoyenneTrimestreRepository extends JpaRepository<MoyenneTrimestre, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici

    MoyenneTrimestre findByClasseAndEleveAndTrimestre(Classe classe, Eleve eleve, Trimestre trimestre);

   Set<MoyenneTrimestre> findByEleveAndTrimestre(Eleve eleve, Trimestre trimestre);

    MoyenneTrimestre findFirstByEleveAndTrimestre(Eleve eleve, Trimestre trimestre);

    List<MoyenneTrimestre> findByEleveAndTrimestre_AnneeAcademique(Eleve eleve, AnneeAcademique anneeAcademique);
}
