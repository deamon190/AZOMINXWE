package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.ClasseMatiere;
import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.model.Note;
import com.classroom.azominxwe.model.Trimestre;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // Méthodes de recherche personnalisées peuvent être ajoutées ici
    // Récupérer les notes par trimestre actif
    List<Note> findByTrimestreActifTrueOrderByEleve();

    List<Note> findByTrimestre_TrimestreId(Long trimestreId);

    @Query("SELECT n.classeMatiere.classeMatiereId FROM Note n WHERE n.eleve.eleveId = :eleveId and n.trimestre.actif=true")
    List<Long> findClasseMatiereIdsByEleveId(Long eleveId);

    @Query("SELECT n.classeMatiere.classeMatiereId FROM Note n WHERE n.eleve.eleveId = :eleveId and n.trimestre.actif=true")
    List <Long> findFirstClasseMatiereIdByEleveId(@Param("eleveId") Long eleveId, PageRequest pageRequest);

    //Récuperer les classesM avec la contrainte de classe existante
    @Query("SELECT n.classeMatiere.classeMatiereId FROM Note n WHERE n.eleve.eleveId = :eleveId and n.classeMatiere.classe = :classeId and n.trimestre.actif=true")
    List<ClasseMatiere> findClasseMatiereIdsByEleveIdAndClasse(Long eleveId, Long classeId);

    List<Note> findByEleveAndTrimestre(Eleve eleve, Trimestre trimestre);

}
