package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.*;
import com.classroom.azominxwe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

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


        List<MoyenneTrimestre> moyennes = moyenneTrimestreRepository.findByEleveAndTrimestre_AnneeAcademique(eleve, anneeAcademiqueRepository.findByActifTrue());

        if (trimestreActif == null) {
            double moyenneAnnuelle = moyennes.stream()
                    .mapToDouble(MoyenneTrimestre::getMoyenne)
                    .average()
                    .orElse(0.0);
            logger.info("Moyenne annuelle pour le matricule " + matricule + " : " + moyenneAnnuelle);
            return "Moyenne annuelle pour le matricule " + matricule + " : " + moyenneAnnuelle;
        }

        LocalDate currentDate = LocalDate.now();
        String trimestreNom = trimestreActif.getNom();

        if (trimestreNom.equals("Trimestre 1") || currentDate.isBefore(convertToLocalDate(trimestreActif.getDateDebut()))) {
            logger.warning("Trimestre 1 ou avant début de trimestre actif, aucune moyenne antérieure");
            return "La moyenne du trimestre précédent n'est pas calculée";
        } else if (trimestreNom.equals("Trimestre 2")) {
            return getMoyenneTrimestrePrecedent(moyennes, "Trimestre 1", matricule);
        } else if (trimestreNom.equals("Trimestre 3")) {
            return getMoyenneTrimestrePrecedent(moyennes, "Trimestre 2", matricule);
        } else if (currentDate.isAfter(convertToLocalDate(trimestreActif.getDateFin()))) {
            double moyenneAnnuelle = moyennes.stream()
                    .mapToDouble(MoyenneTrimestre::getMoyenne)
                    .average()
                    .orElse(0.0);
            logger.info("Moyenne annuelle pour le matricule " + matricule + " : " + moyenneAnnuelle);
            return "Moyenne annuelle pour le matricule " + matricule + " : " + moyenneAnnuelle;
        } else {
            return "Erreur dans la détermination du trimestre actif.";
        }
    }

    private String getMoyenneTrimestrePrecedent(List<MoyenneTrimestre> moyennes, String trimestrePrecedentNom, String matricule) {
        double moyenne = moyennes.stream()
                .filter(m -> m.getTrimestre().getNom().equals(trimestrePrecedentNom))
                .mapToDouble(MoyenneTrimestre::getMoyenne)
                .findFirst()
                .orElse(0.0);

        if (moyenne == 0.0) {
            logger.warning("La moyenne du " + trimestrePrecedentNom + " n'est pas calculée pour le matricule : " + matricule);
            return "La moyenne du " + trimestrePrecedentNom + " n'est pas calculée";
        } else {
            logger.info("Moyenne du " + trimestrePrecedentNom + " pour le matricule " + matricule + " : " + moyenne);
            return "Moyenne du " + trimestrePrecedentNom + " pour le matricule " + matricule + " : " + moyenne;
        }
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
            return "BAucun trimestre actif trouvé pour l'année académique en cours";
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
        //logger.info(trimestreRepository.findByNomAndAnneeAcademique("Trimestre 3",anneeAcademiqueRepository.findByActifTrue()).toString());
        return null;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
