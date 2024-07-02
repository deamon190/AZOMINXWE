package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici
    @Query("SELECT m FROM Matiere m ORDER BY m.nomMatiere, m.nomCourtMatiere")
    public List<Matiere> findAll();

    boolean existsByNomMatiere(String nomMatiere);

    boolean existsByNomCourtMatiere(String NomCourtMatiere);

}
