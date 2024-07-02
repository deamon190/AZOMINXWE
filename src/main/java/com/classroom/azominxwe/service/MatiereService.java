package com.classroom.azominxwe.service;

import com.classroom.azominxwe.dto.MatiereDTO;
import com.classroom.azominxwe.model.Matiere;
import com.classroom.azominxwe.repository.ClasseMatiereRepository;
import com.classroom.azominxwe.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatiereService {

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private ClasseMatiereRepository classeMatiereRepository;

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Optional<Matiere> getMatiereById(Long id) {
        return matiereRepository.findById(id);
    }

    public void update(Long id, Matiere matiere) {
        Matiere existingMatiere = matiereRepository.findById(id).orElse(null);
        if (existingMatiere != null) {
            existingMatiere.setNomMatiere(matiere.getNomMatiere());
            matiereRepository.save(existingMatiere);
        }
    }


    public Matiere saveMatiere(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    public void deleteMatiere(Long id) {
        matiereRepository.deleteById(id);
    }


    @Transactional
    public List<MatiereDTO> getMatieresForClasse(Long classeId) {

        List<Long> MatiereIdsUsed = classeMatiereRepository.findMatiereIdsByClasseId(classeId);
            return getAllMatieres()
                    .stream()
                    .filter(matiere -> !MatiereIdsUsed.contains(matiere.getMatiereId()))
                    .map(matiere -> new MatiereDTO(
                            matiere.getMatiereId(),
                            matiere.getNomMatiere(),
                            matiere.getNomCourtMatiere()
                    ))
                    .collect(Collectors.toList());

    }
}
