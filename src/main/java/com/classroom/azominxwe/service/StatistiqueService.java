package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.AnneeAcademique;
import com.classroom.azominxwe.model.Trimestre;
import com.classroom.azominxwe.repository.ClasseRepository;
import com.classroom.azominxwe.repository.AnneeAcademiqueRepository;
import com.classroom.azominxwe.repository.EnseignantRepository;
import com.classroom.azominxwe.repository.EleveRepository;
import com.classroom.azominxwe.repository.TrimestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatistiqueService {
    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private ClasseRepository classeRepository;

    public Trimestre getTrimestreActif() {
        return trimestreRepository.findByActifTrue();
    }

    public long getNombreEleves() {
        return eleveRepository.count();
    }

    public long getNombreEnseignants() {
        return enseignantRepository.count();
    }

    public long getNombreClasses() {
        return classeRepository.count();
    }

public long getNombreAnneeAcademiques() {
    return anneeAcademiqueRepository.count();
}

public long getNombreTrimestres() {
        return trimestreRepository.count();
    }


    public boolean isAnneeAcademiqueActive(Long id) {
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueRepository.findById(id);
        return anneeAcademique.map(AnneeAcademique::getActif).orElse(false);
    }



}
