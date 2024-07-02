package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.Enseignement;
import com.classroom.azominxwe.repository.EnseignementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignementService {

    @Autowired
    private EnseignementRepository enseignementRepository;

    public List<Enseignement> getAllEnseignements() {
        return enseignementRepository.findAll();
    }

    public List<Enseignement> Enseignements_anneeactive() {
        return enseignementRepository.findByAnneeAcademiqueActifTrue();
    }

    public Optional<Enseignement> getEnseignementById(Long id) {
        return enseignementRepository.findById(id);
    }


    public Enseignement saveEnseignement(Enseignement enseignement) {
        return enseignementRepository.save(enseignement);
    }

    public void deleteEnseignement(Long id) {
        enseignementRepository.deleteById(id);
    }

    public void updateEnseignement(Long id, Enseignement enseignementDetails) {
        Optional<Enseignement> existingEnseignement = enseignementRepository.findById(id);
        if (existingEnseignement.isPresent()) {
            Enseignement enseignement = existingEnseignement.get();
            enseignement.setEnseignant(enseignementDetails.getEnseignant());
            enseignement.setClasseMatiere(enseignementDetails.getClasseMatiere());
            enseignement.setAnneeAcademique(enseignementDetails.getAnneeAcademique());
            enseignementRepository.save(enseignement);
        }
    }
}
