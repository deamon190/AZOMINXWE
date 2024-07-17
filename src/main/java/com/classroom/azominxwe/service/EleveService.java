package com.classroom.azominxwe.service;

import com.classroom.azominxwe.dto.EleveDTO;
import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.repository.EleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EleveService {

    @Autowired
    private EleveRepository eleveRepository;

    // Méthode pour obtenir tous les élèves
    public List<Eleve> getAllEleves() {
        return eleveRepository.findAll();
    }

    // Méthode pour obtenir tous les élèves sous forme de DTO
    public List<EleveDTO> getAllEleveDTOs() {
        return eleveRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Méthode pour obtenir un élève par ID
    public Optional<Eleve> getEleveById(Long id) {
        return eleveRepository.findById(id);
    }

    // Méthode pour obtenir un élève par ID sous forme de DTO
    public Optional<EleveDTO> getEleveDTOById(Long id) {
        return eleveRepository.findById(id).map(this::convertToDTO);
    }

    // Méthode pour obtenir un élève par matricule sous forme de DTO
    public Optional<EleveDTO> getEleveDTOByMatricule(String oldMatricule) {
        return eleveRepository.findByMatricule(oldMatricule).map(this::convertToDTO);
    }

    // Méthode pour sauvegarder un élève sous forme de DTO
    public EleveDTO saveEleveDTO(EleveDTO eleveDTO) {
        Eleve eleve = convertToEntity(eleveDTO);
        return convertToDTO(eleveRepository.save(eleve));
    }

    // Méthode pour mettre à jour un élève sous forme de DTO
    public void updateDTO(Long id, EleveDTO eleveDTO) {
        Eleve existingEleve = eleveRepository.findById(id).orElse(null);
        if (existingEleve != null) {
            existingEleve.setNom(eleveDTO.getNom());
            existingEleve.setPrenom(eleveDTO.getPrenom());
            existingEleve.setMatricule(eleveDTO.getMatricule());
            eleveRepository.save(existingEleve);
        }
    }






    // Méthode pour vérifier si un élève existe par matricule
    public boolean checkbyMatricule(String oldMatricule) {
        return eleveRepository.existsByMatricule(oldMatricule);
    }

    // Méthode pour obtenir un élève par matricule
    public Eleve getEleveByMatricule(String oldMatricule) {
        return eleveRepository.getEleveByMatricule(oldMatricule);
    }



    // Méthode pour sauvegarder un élève
    public Eleve saveEleve(Eleve eleve) {
        return eleveRepository.save(eleve);
    }



    // Méthode pour mettre à jour un élève
    public void update(Long id, Eleve eleve) {
        Eleve existingEleve = eleveRepository.findById(id).orElse(null);
        if (existingEleve != null) {
            existingEleve.setNom(eleve.getNom());
            existingEleve.setPrenom(eleve.getPrenom());
            existingEleve.setMatricule(eleve.getMatricule());
            eleveRepository.save(existingEleve);
        }
    }

    // Méthode pour supprimer un élève
    public void deleteEleve(Long id) {
        eleveRepository.deleteById(id);
    }

    // Méthode pour convertir un Eleve en EleveDTO
    private EleveDTO convertToDTO(Eleve eleve) {
        EleveDTO eleveDTO = new EleveDTO();
        eleveDTO.setEleveId(eleve.getEleveId());
        eleveDTO.setNom(eleve.getNom());
        eleveDTO.setPrenom(eleve.getPrenom());
        eleveDTO.setMatricule(eleve.getMatricule());
        return eleveDTO;
    }

    // Méthode pour convertir un EleveDTO en Eleve
    private Eleve convertToEntity(EleveDTO eleveDTO) {
        Eleve eleve = new Eleve();
        eleve.setEleveId(eleveDTO.getEleveId());
        eleve.setNom(eleveDTO.getNom());
        eleve.setPrenom(eleveDTO.getPrenom());
        eleve.setMatricule(eleveDTO.getMatricule());
        return eleve;
    }
}
