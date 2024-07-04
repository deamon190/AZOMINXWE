package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Trimestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrimestreRepository extends JpaRepository<Trimestre, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici
    Trimestre findByActifTrue();
    Long countByActif(boolean b);
    List<Trimestre> findByAnneeAcademique_AnneeAcademiqueId(Long anneeAcademiqueId);
    List<Trimestre> findByAnneeAcademique_ActifTrue();

    Trimestre findByAnneeAcademique_ActifTrueAndActifTrue();
Trimestre getByTrimestreId(Long Id);

}
