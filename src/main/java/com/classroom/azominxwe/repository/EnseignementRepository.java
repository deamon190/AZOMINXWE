package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Enseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici
    // Récupérer les enseignements par année académique active
    List<Enseignement> findByAnneeAcademiqueActifTrue();
}
