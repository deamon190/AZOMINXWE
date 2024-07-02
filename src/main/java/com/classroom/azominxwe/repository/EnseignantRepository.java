package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici
}
