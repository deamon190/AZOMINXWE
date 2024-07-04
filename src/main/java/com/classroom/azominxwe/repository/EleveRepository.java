package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici

    Eleve getEleveByMatricule(String newMatricule);

    boolean existsByMatricule(String newMatricule);

    Optional<Eleve> findByMatricule(String newMatricule);

}
