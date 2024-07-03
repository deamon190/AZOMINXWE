package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.*;
import com.classroom.azominxwe.repository.ClasseMatiereRepository;
import com.classroom.azominxwe.repository.MoyenneTrimestreRepository;
import com.classroom.azominxwe.repository.NoteRepository;
import com.classroom.azominxwe.repository.MoyenneMatiereRepository;
import jakarta.transaction.Transactional;
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


    @Autowired
    private MoyenneMatiereRepository moyenneMatiereRepository;


    public List<MoyenneTrimestre> getAllMoyennesTrimestre() {
        return moyenneTrimestreRepository.findAll();
    }

    public Optional<MoyenneTrimestre> getMoyenneTrimestreById(Long id) {
        return moyenneTrimestreRepository.findById(id);
    }


        @Transactional
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

                        // Supprimer les moyennes trimestrielles obsolètes pour l'élève et le trimestre actif
                        Trimestre trimestreActif = trimestreService.getByTrimestreId(trimestreId);
                        moyenneTrimestreRepository.deleteByEleveAndTrimestreAndClasseNot(eleve, trimestreActif, notesEleve.get(0).getClasseMatiere().getClasse());

                        double totalCoef = 0.0;
                        double totalNoteCoef = 0.0;

                        for (ClasseMatiere classeMatiere : matieresClasse) {
                            // Calcul des moyennes par matière
                            double coef = classeMatiere.getCoefficient();
                            double moyenneNote = notesEleve.stream()
                                    .filter(note -> note.getClasseMatiere().equals(classeMatiere))
                                    .collect(Collectors.averagingDouble(Note::getNote));

                            // Rechercher s'il existe déjà une MoyenneMatiere pour cette combinaison
                            Optional<MoyenneMatiere> moyenneMatiereOpt = moyenneMatiereRepository.findByEleveAndClasseMatiereAndTrimestre(eleve, classeMatiere, trimestreActif);

                            MoyenneMatiere moyenneMatiere;
                            if (moyenneMatiereOpt.isPresent()) {
                                moyenneMatiere = moyenneMatiereOpt.get();
                            } else {
                                moyenneMatiere = new MoyenneMatiere();
                                moyenneMatiere.setEleve(eleve);
                                moyenneMatiere.setClasseMatiere(classeMatiere);
                                moyenneMatiere.setTrimestre(trimestreActif);
                            }

                            // Mettre à jour la moyenne par matière
                            moyenneMatiere.setMoyenne(moyenneNote);
                            moyenneMatiereRepository.save(moyenneMatiere);

                            totalNoteCoef += moyenneNote * coef;
                        }

                        // Ajouter les matières sans note avec une moyenne de 0
                        List<ClasseMatiere> allClasseMatieres = classeMatiereRepository.findByClasse_ClasseId(notesEleve.get(0).getClasseMatiere().getClasse().getClasseId());
                        for (ClasseMatiere classeMatiere : allClasseMatieres) {
                            if (!matieresClasse.contains(classeMatiere)) {
                                Optional<MoyenneMatiere> moyenneMatiereOpt = moyenneMatiereRepository.findByEleveAndClasseMatiereAndTrimestre(eleve, classeMatiere, trimestreActif);
                                MoyenneMatiere moyenneMatiere;
                                if (moyenneMatiereOpt.isPresent()) {
                                    moyenneMatiere = moyenneMatiereOpt.get();
                                } else {
                                    moyenneMatiere = new MoyenneMatiere();
                                    moyenneMatiere.setEleve(eleve);
                                    moyenneMatiere.setClasseMatiere(classeMatiere);
                                    moyenneMatiere.setTrimestre(trimestreActif);
                                }
                                moyenneMatiere.setMoyenne(0.0);
                                moyenneMatiereRepository.save(moyenneMatiere);
                            }
                        }

                        Classe classe = notesEleve.get(0).getClasseMatiere().getClasse();
                        totalCoef =classeMatiereRepository.findSumCoefficientByClasseId(classe.getClasseId());

                        double moyenne = totalCoef > 0 ? totalNoteCoef / totalCoef : 0;

                        // Rechercher s'il existe déjà une MoyenneTrimestre pour cette combinaison
                        Optional<MoyenneTrimestre> moyenneTrimestreOpt = moyenneTrimestreRepository
                                .findByClasseAndEleveAndTrimestre(classe, eleve, trimestreActif);

                        MoyenneTrimestre moyenneTrimestre;
                        if (moyenneTrimestreOpt.isPresent()) {
                            moyenneTrimestre = moyenneTrimestreOpt.get();
                        } else {
                            moyenneTrimestre = new MoyenneTrimestre();
                            moyenneTrimestre.setEleve(eleve);
                            moyenneTrimestre.setClasse(classe);
                            moyenneTrimestre.setTrimestre(trimestreActif);
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
