package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici
    List<Classe> findByAnneeAcademique_ActifTrue();


}
