package com.classroom.azominxwe.repository;

import com.classroom.azominxwe.model.ClasseMatiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseMatiereRepository extends JpaRepository<ClasseMatiere, Long> {
    List<ClasseMatiere> findByClasse_AnneeAcademique_ActifTrue();
    Optional<ClasseMatiere> findByClasse_ClasseIdAndMatiere_MatiereIdAndClasse_AnneeAcademique_ActifTrue(Long classeId, Long matiereId);
    @Query("SELECT cm FROM ClasseMatiere cm WHERE cm.classe.anneeAcademique.actif = true ORDER BY cm.classe.nomClasse, cm.matiere.nomMatiere")
    List<ClasseMatiere> findByClasse_AnneeAcademique_ActifTrueOrderByClasseNomClasseAscMatiereNomMatiereAsc();


    @Query("SELECT cm FROM ClasseMatiere cm WHERE cm.classe.anneeAcademique.actif = true AND cm NOT IN (SELECT e.classeMatiere FROM Enseignement e) " +
            " ORDER BY cm.classe.nomClasse, cm.matiere.nomMatiere")
    List<ClasseMatiere> findUnassignedClassesByAnneeAcademiqueActive();



    // récupérer ClasseMatiere par classeId
    List<ClasseMatiere> findByClasse_ClasseId(Long classeId);

    // récupérer ClasseMatiere par classeId
    ClasseMatiere findByClasseMatiereId(Long classeMatId);

    //Récuperer les classesM avec la contrainte de classe existante
    @Query("SELECT n FROM ClasseMatiere n WHERE n.classe.classeId = :classeId and n.classe.anneeAcademique.actif=true")
    List<ClasseMatiere> findClasseMatiereByClasseId(Long classeId);

    @Query("SELECT n.matiere.matiereId FROM ClasseMatiere n WHERE n.classe.classeId = :classeId and n.classe.anneeAcademique.actif=true")
    List<Long> findMatiereIdsByClasseId(Long classeId);

    // Récupérer la somme des coefficients par classeId
    @Query("SELECT SUM(cm.coefficient) FROM ClasseMatiere cm WHERE cm.classe.classeId = :classeId")
    Double findSumCoefficientByClasseId(Long classeId);

}
