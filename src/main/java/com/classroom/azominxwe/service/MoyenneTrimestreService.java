package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.*;
import com.classroom.azominxwe.repository.ClasseMatiereRepository;
import com.classroom.azominxwe.repository.MoyenneTrimestreRepository;
import com.classroom.azominxwe.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoyenneTrimestreService {

    @Autowired
    private MoyenneTrimestreRepository moyenneTrimestreRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ClasseMatiereRepository classeMatiereRepository;

    @Autowired
    private TrimestreService trimestreService;


    public List<MoyenneTrimestre> getAllMoyennesTrimestre() {
        return moyenneTrimestreRepository.findAll();
    }

    public Optional<MoyenneTrimestre> getMoyenneTrimestreById(Long id) {
        return moyenneTrimestreRepository.findById(id);
    }

    public void calculerMoyenneParTrimestre(Long trimestreId) {
        List<Note> notes = noteRepository.findByTrimestre_TrimestreId(trimestreId);

        // Récupérer toutes les matières pour les classes concernées par le trimestre
        Map<Eleve, Set<ClasseMatiere>> matieresParClasse = notes.stream()
                .collect(Collectors.groupingBy(
                        Note::getEleve,
                        Collectors.mapping(Note::getClasseMatiere, Collectors.toSet())
                ));

        // Calcul des moyennes
        notes.stream()
                .collect(Collectors.groupingBy(Note::getEleve))
                .forEach((eleve, notesEleve) -> {
                    Set<ClasseMatiere> matieresClasse = matieresParClasse.getOrDefault(eleve, Collections.emptySet());

                    Map<ClasseMatiere, Double> notesParMatiere = notesEleve.stream()
                            .collect(Collectors.toMap(Note::getClasseMatiere, Note::getNote));

                    double totalCoef = 0.0;
                    double totalNoteCoef = 0.0;

                    for (ClasseMatiere classeMatiere : matieresClasse) {
                        double coef = classeMatiere.getCoefficient();
                        double note = notesParMatiere.getOrDefault(classeMatiere, 0.0);

                        totalNoteCoef += note * coef;
                    }

                    Classe classe = notesEleve.get(0).getClasseMatiere().getClasse();
                    totalCoef = classeMatiereRepository.findSumCoefficientByClasseId(classe.getClasseId());

                    double moyenne = totalCoef > 0 ? totalNoteCoef / totalCoef : 0;

                    // Rechercher s'il existe déjà une MoyenneTrimestre pour cette combinaison
                    MoyenneTrimestre moyenneTrimestre = moyenneTrimestreRepository
                            .findByClasseAndEleveAndTrimestre(classe, eleve, trimestreService.getByTrimestreId(trimestreId));

                    if (moyenneTrimestre == null) {
                        moyenneTrimestre = new MoyenneTrimestre();
                        moyenneTrimestre.setEleve(eleve);
                        moyenneTrimestre.setClasse(classe);
                        moyenneTrimestre.setTrimestre(trimestreService.getByTrimestreId(trimestreId));
                    }

                    moyenneTrimestre.setMoyenne(moyenne);

                    moyenneTrimestreRepository.save(moyenneTrimestre);
                });
    }

    public MoyenneTrimestre saveMoyenneTrimestre(MoyenneTrimestre moyenneTrimestre) {
        return moyenneTrimestreRepository.save(moyenneTrimestre);
    }

    public void deleteMoyenneTrimestre(Long id) {
        moyenneTrimestreRepository.deleteById(id);
    }

}
