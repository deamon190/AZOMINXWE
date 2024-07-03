package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.ClasseMatiere;
import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.model.MoyenneMatiere;
import com.classroom.azominxwe.model.Trimestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoyenneMatiereRepository extends JpaRepository<MoyenneMatiere, Long> {
    List<MoyenneMatiere> findByEleveAndTrimestre(Eleve eleve, Trimestre trimestre);
    void deleteByEleveAndTrimestre(Eleve eleve, Trimestre trimestre);

    Optional<MoyenneMatiere> findByEleveAndClasseMatiereAndTrimestre(Eleve eleve, ClasseMatiere classeMatiere, Trimestre trimestreActif);

}
