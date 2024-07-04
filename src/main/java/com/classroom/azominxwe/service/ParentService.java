package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.*;
import com.classroom.azominxwe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ParentService {

    private static final Logger logger = Logger.getLogger(ParentService.class.getName());

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private MoyenneTrimestreRepository moyenneTrimestreRepository;

    @Autowired
    private MoyenneMatiereRepository moyenneMatiereRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private ClasseMatiereRepository classeMatiereRepository;

    public String getMoyenneByMatricule(String matricule) {
        logger.info("Recherche de la moyenne pour le matricule : " + matricule);
        Eleve eleve = eleveRepository.getEleveByMatricule(matricule);

        if (eleve == null) {
            logger.warning("Matricule invalide : " + matricule);
            return "Matricule invalide";
        }

        Trimestre trimestreActif = getTrimestreActif();
        if (trimestreActif == null) {
            logger.warning("Aucun trimestre actif trouvé pour l'année académique en cours");
            return "Aucun trimestre actif trouvé pour l'année académique en cours";
        }

        List<MoyenneTrimestre> moyennes = moyenneTrimestreRepository.findByEleveAndTrimestre_AnneeAcademique(eleve, trimestreActif.getAnneeAcademique());

        // Si on est en fin d'année, calculer la moyenne annuelle
        if (trimestreActif.getNom().equals("Trimestre 3")) {
            double moyenneAnnuelle = moyennes.stream()
                    .mapToDouble(MoyenneTrimestre::getMoyenne)
                    .average()
                    .orElse(0.0);
            logger.info("Moyenne annuelle pour le matricule " + matricule + " : " + moyenneAnnuelle);
            return String.valueOf(moyenneAnnuelle);
        }

        // Sinon, retourner la moyenne du trimestre précédent
        Trimestre trimestrePrecedent = getTrimestrePrecedent(trimestreActif);
        if (trimestrePrecedent == null) {
            logger.warning("Aucun trimestre précédent trouvé");
            return "Aucun trimestre précédent trouvé";
        }

        Set<MoyenneTrimestre> moyennesPrecedentes = moyenneTrimestreRepository.findByEleveAndTrimestre(eleve, trimestrePrecedent);

        double moyennePrecedente = moyennesPrecedentes.stream()
                .map(MoyenneTrimestre::getMoyenne)
                .findFirst()
                .orElse(0.0);
        logger.info("Moyenne du trimestre précédent pour le matricule " + matricule + " : " + moyennePrecedente);
        return String.valueOf(moyennePrecedente);
    }

    public String getMoyenneByMatriculeAndMatiere(String matricule, Long matiereId) {
        logger.info("Recherche de la moyenne pour le matricule : " + matricule + " et la matière : " + matiereId);
        Eleve eleve = eleveRepository.getEleveByMatricule(matricule);

        if (eleve == null) {
            logger.warning("Matricule invalide : " + matricule);
            return "Matricule invalide";
        }

        Trimestre trimestreActif = getTrimestreActif();
        if (trimestreActif == null) {
            logger.warning("Aucun trimestre actif trouvé pour l'année académique en cours");
            return "Aucun trimestre actif trouvé pour l'année académique en cours";
        }

        ClasseMatiere classeMatiere = classeMatiereRepository.findById(matiereId)
                .orElse(null);

        if (classeMatiere == null) {
            logger.warning("Matière invalide : " + matiereId);
            return "Matière invalide";
        }

        MoyenneMatiere moyenneMatiere = moyenneMatiereRepository.findByEleveAndClasseMatiereAndTrimestre(eleve, classeMatiere, trimestreActif)
                .orElse(null);

        if (moyenneMatiere == null) {
            logger.warning("Aucune moyenne trouvée pour la matière " + matiereId);
            return "Aucune moyenne trouvée pour cette matière";
        }

        logger.info("Moyenne pour le matricule " + matricule + " et la matière " + matiereId + " : " + moyenneMatiere.getMoyenne());
        return String.valueOf(moyenneMatiere.getMoyenne());
    }

    private Trimestre getTrimestreActif() {
        List<Trimestre> trimestresActifs = trimestreRepository.findByAnneeAcademique_ActifTrue();
        LocalDate currentDate = LocalDate.now();

        logger.info("Recherche du trimestre actif à la date : " + currentDate);
        for (Trimestre trimestre : trimestresActifs) {
            LocalDate dateDebut = convertToLocalDate(trimestre.getDateDebut());
            LocalDate dateFin = convertToLocalDate(trimestre.getDateFin());
            logger.info("Vérification du trimestre : " + trimestre.getNom() + " (" + dateDebut + " - " + dateFin + ")");
            if ((currentDate.isEqual(dateDebut) || currentDate.isAfter(dateDebut)) &&
                    (currentDate.isEqual(dateFin) || currentDate.isBefore(dateFin))) {
                return trimestre;
            }
        }
        return null;
    }

    private Trimestre getTrimestrePrecedent(Trimestre trimestreActif) {
        List<Trimestre> trimestres = trimestreRepository.findByAnneeAcademique_AnneeAcademiqueId(trimestreActif.getAnneeAcademique().getAnneeAcademiqueId());
        for (Trimestre trimestre : trimestres) {
            if (trimestre.getNom().equals(trimestreActif.getNom() + " - 1")) {
                return trimestre;
            }
        }
        return null;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
