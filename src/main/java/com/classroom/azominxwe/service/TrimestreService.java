package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.Trimestre;
import com.classroom.azominxwe.repository.TrimestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrimestreService {

    @Autowired
    private TrimestreRepository trimestreRepository;

    public List<Trimestre> getAllTrimestres() {
        return trimestreRepository.findAll();
    }

    public Trimestre getByTrimestreId(Long Id){ return trimestreRepository.getByTrimestreId(Id);}

    public Optional<Trimestre> getTrimestreById(Long id) {
        return trimestreRepository.findById(id);
    }


    public List<Trimestre> getTrimestresByAnneeAcademique(Long anneeAcademiqueId) {
        return trimestreRepository.findByAnneeAcademique_AnneeAcademiqueId(anneeAcademiqueId);
    }

    public void update(Long id, Trimestre trimestre) {
        Trimestre existingTrimestre = trimestreRepository.findById(id).orElse(null);
        if (existingTrimestre != null) {
            //existingTrimestre.setNom(trimestre.getNom());
            existingTrimestre.setDateDebut(trimestre.getDateDebut());
            existingTrimestre.setDateFin(trimestre.getDateFin());
            existingTrimestre.setAnneeAcademique(trimestre.getAnneeAcademique());
            trimestreRepository.save(existingTrimestre);
        }
    }

    public Trimestre saveTrimestre(Trimestre trimestre) {
        return trimestreRepository.save(trimestre);
    }

    public void deleteTrimestre(Long id) {
        trimestreRepository.deleteById(id);
    }

    public void activateTrimestre(Long id) {
        // Désactiver toutes les années académiques
        List<Trimestre> allTrimestres = trimestreRepository.findAll();
        for (Trimestre trimestre : allTrimestres) {

            if (trimestre.getTrimestreId()==id)
            {
                trimestre.setActif(true);
            }
            else
            {
                trimestre.setActif(false);
            }
            trimestreRepository.save(trimestre);
        }

    }

    public Trimestre getActiveTrimestre() {
        return trimestreRepository.findByActifTrue();
    }


    public List<Trimestre> getTrimestresByAnneeAcademiqueActive() {
        return trimestreRepository.findByAnneeAcademique_ActifTrue();
    }

    public Trimestre getActiveTrimestresForActiveAnneeAcademique() {
        return trimestreRepository.findByAnneeAcademique_ActifTrueAndActifTrue();
    }

}
