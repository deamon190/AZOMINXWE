package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.AnneeAcademique;
import com.classroom.azominxwe.repository.AnneeAcademiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnneeAcademiqueService {

    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;
    public List<AnneeAcademique> getAllAnneesAcademiques() {
        return anneeAcademiqueRepository.findAll();
    }

    public Optional<AnneeAcademique> getAnneeAcademiqueById(Long id) {
        return anneeAcademiqueRepository.findById(id);
    }

    public AnneeAcademique saveAnneeAcademique(AnneeAcademique anneeAcademique) {
        return anneeAcademiqueRepository.save(anneeAcademique);
    }

    public void update(Long id, AnneeAcademique anneeAcademique) {
        AnneeAcademique existingAnneeAcademique = anneeAcademiqueRepository.findById(id).orElse(null);
        if (existingAnneeAcademique != null) {
            existingAnneeAcademique.setAnnee(anneeAcademique.getAnnee());
            anneeAcademiqueRepository.save(existingAnneeAcademique);
        }
    }

    public void activateAnneeAcademique(Long id) {
        // Désactiver toutes les années académiques
        List<AnneeAcademique> allAnnees = anneeAcademiqueRepository.findAll();
        for (AnneeAcademique annee : allAnnees) {
            annee.setActif(false);
            anneeAcademiqueRepository.save(annee);
        }

        // Activer l'année académique spécifiée
        Optional<AnneeAcademique> existingAnneeAcademiqueOpt = anneeAcademiqueRepository.findById(id);
        if (existingAnneeAcademiqueOpt.isPresent()) {
            AnneeAcademique existingAnneeAcademique = existingAnneeAcademiqueOpt.get();
            existingAnneeAcademique.setActif(true);
            anneeAcademiqueRepository.save(existingAnneeAcademique);
        }
    }

    public void deleteAnneeAcademique(Long id) {
        anneeAcademiqueRepository.deleteById(id);
    }

    public AnneeAcademique getActiveAnneeAcademique() {
        return anneeAcademiqueRepository.findByActifTrue();
    }

}
