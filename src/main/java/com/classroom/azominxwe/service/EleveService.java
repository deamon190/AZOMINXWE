package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.repository.EleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EleveService {

    @Autowired
    private EleveRepository eleveRepository;

    public List<Eleve> getAllEleves() {
        return eleveRepository.findAll();
    }

    public Optional<Eleve> getEleveById(Long id) {
        return eleveRepository.findById(id);
    }

    public boolean checkbyMatricule (String oldMatricule)
    {
        return eleveRepository.existsByMatricule(oldMatricule);
    }

    public Eleve getEleveByMatricule (String oldMatricule)
    {
        return  eleveRepository.getEleveByMatricule(oldMatricule);
    }


    public void update(Long id, Eleve eleve) {
        Eleve existingEleve = eleveRepository.findById(id).orElse(null);
        if (existingEleve != null) {
            existingEleve.setNom(eleve.getNom());
            existingEleve.setPrenom(eleve.getPrenom());
            existingEleve.setMatricule(eleve.getMatricule());
            eleveRepository.save(existingEleve);
        }
    }

    public Eleve saveEleve(Eleve eleve) {
        return eleveRepository.save(eleve);
    }

    public void deleteEleve(Long id) {
        eleveRepository.deleteById(id);
    }
}
