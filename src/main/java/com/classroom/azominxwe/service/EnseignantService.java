package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.Enseignant;
import com.classroom.azominxwe.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    public Optional<Enseignant> getEnseignantById(Long id) {
        return enseignantRepository.findById(id);
    }

    public Enseignant saveEnseignant(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }

    public void update(Long id, Enseignant updatedEnseignant) {
        Optional<Enseignant> optionalEnseignant = enseignantRepository.findById(id);
        if (optionalEnseignant.isPresent()) {
            Enseignant enseignant = optionalEnseignant.get();
            enseignant.setPrenom(updatedEnseignant.getPrenom());
            enseignant.setNom(updatedEnseignant.getNom());
            enseignant.setEmail(updatedEnseignant.getEmail());
            enseignantRepository.save(enseignant);
        }
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }


}
