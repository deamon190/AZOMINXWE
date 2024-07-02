package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.Classe;
import com.classroom.azominxwe.repository.ClasseMatiereRepository;
import com.classroom.azominxwe.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseService {

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ClasseMatiereRepository classeMatiereRepository;

    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    public List<Classe> getClassesByAnneeAcademiqueActive() {
        return classeRepository.findByAnneeAcademique_ActifTrue();
    }



    public Optional<Classe> getClasseById(Long id) {
        return classeRepository.findById(id);
    }

    public void update(Long id, Classe classe) {
        Classe existingClasse = classeRepository.findById(id).orElse(null);
        if (existingClasse != null) {
            existingClasse.setNomClasse(classe.getNomClasse());
            existingClasse.setAnneeAcademique(classe.getAnneeAcademique());
            classeRepository.save(existingClasse);
        }
    }

    public Classe saveClasse(Classe classe) {
        return classeRepository.save(classe);
    }

    public void deleteClasse(Long id) {
        classeRepository.deleteById(id);
    }
}
