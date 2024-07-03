package com.classroom.azominxwe.service;

import com.classroom.azominxwe.dto.ClasseMatiereDTO;
import com.classroom.azominxwe.model.ClasseMatiere;
import com.classroom.azominxwe.repository.ClasseMatiereRepository;
import com.classroom.azominxwe.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClasseMatiereService {

    @Autowired
    private ClasseMatiereRepository classeMatiereRepository;
    @Autowired
    private NoteRepository noteRepository;

    public List<ClasseMatiere> getAllClasseMatieres() {
        return classeMatiereRepository.findAll();
    }

    public List<ClasseMatiere> getClassesByAnneeAcademiqueActive(){
        return classeMatiereRepository.findByClasse_AnneeAcademique_ActifTrue();
    }

    public List<ClasseMatiere> getClassesOrderedByAnneeAcademiqueActive(){
        return classeMatiereRepository.findByClasse_AnneeAcademique_ActifTrueOrderByClasseNomClasseAscMatiereNomMatiereAsc();
    }

    public List<ClasseMatiere> getUnassignedClassesByAnneeAcademiqueActive() {
        return classeMatiereRepository.findUnassignedClassesByAnneeAcademiqueActive();
    }

    public List<ClasseMatiere> getClassesMatieresByClasseId(Long classeId) {
        return classeMatiereRepository.findByClasse_ClasseId(classeId);
    }

    public Optional<ClasseMatiere> getClasseMatiereById(Long id) {
        return classeMatiereRepository.findById(id);
    }

    public Optional<ClasseMatiere> findByClasseAndMatiereAndAnneeAcademiqueActive(Long classeId, Long matiereId) {
        return classeMatiereRepository.findByClasse_ClasseIdAndMatiere_MatiereIdAndClasse_AnneeAcademique_ActifTrue(classeId, matiereId);
    }

    public void update(Long id, ClasseMatiere classeMatiere) {
        ClasseMatiere existingClasseMatiere = classeMatiereRepository.findById(id).orElse(null);
        if (existingClasseMatiere != null) {
            existingClasseMatiere.setCoefficient(classeMatiere.getCoefficient());
            existingClasseMatiere.setClasse(classeMatiere.getClasse());
            existingClasseMatiere.setMatiere(classeMatiere.getMatiere());
            classeMatiereRepository.save(existingClasseMatiere);
        }
    }

    public ClasseMatiere saveMatiereClasse(ClasseMatiere classeMatiere) {
        return classeMatiereRepository.save(classeMatiere);
    }

    public void deleteMatiereClasse(Long id) {
        classeMatiereRepository.deleteById(id);
    }

    @Transactional
    public List<ClasseMatiereDTO> getClasseMatieresForEleve(Long eleveId) {
        List<Long> classeMatiereIdsWithNotes = noteRepository.findClasseMatiereIdsByEleveId(eleveId);
        Long firstClasseMatiere = getFirstClasseMatiereIdForEleve(eleveId);

        if (firstClasseMatiere == null) {
            // Aucun enregistrement d'abord
            return classeMatiereRepository.findByClasse_AnneeAcademique_ActifTrue()
                    .stream()
                    .map(classeMatiere -> new ClasseMatiereDTO(
                            classeMatiere.getClasseMatiereId(),
                            classeMatiere.getClasse().getNomClasse(),
                            classeMatiere.getMatiere().getNomCourtMatiere()
                    ))
                    .collect(Collectors.toList());
        } else {
            ClasseMatiere classMTmp = classeMatiereRepository.findByClasseMatiereId(firstClasseMatiere);
            return classeMatiereRepository.findClasseMatiereByClasseId(classMTmp.getClasse().getClasseId())
                    .stream()
                    .map(classeMatiere -> new ClasseMatiereDTO(
                            classeMatiere.getClasseMatiereId(),
                            classeMatiere.getClasse().getNomClasse(),
                            classeMatiere.getMatiere().getNomCourtMatiere()
                    ))
                    .collect(Collectors.toList());
        }
    }



    public Long getFirstClasseMatiereIdForEleve(Long eleveId) {
        List<Long> classeMatiereIds = noteRepository.findFirstClasseMatiereIdByEleveId(eleveId, PageRequest.of(0, 1));
        return classeMatiereIds.isEmpty() ? null : classeMatiereIds.get(0);
    }




}
