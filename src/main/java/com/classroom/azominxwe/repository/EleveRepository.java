package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici

    boolean existsByMatricule(String newMatricule);

    Eleve getEleveByMatricule(String newMatricule);
}
