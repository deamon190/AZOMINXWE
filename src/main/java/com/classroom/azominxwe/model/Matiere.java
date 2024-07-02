package com.classroom.azominxwe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matiereId;

    @NotBlank(message = "Le nom de la matière ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la matière doit contenir entre 2 et 100 caractères")
    @Column(unique = true)
    private String nomMatiere;

    @NotBlank(message = "Le nom court de la matière ne peut pas être vide")
    @Size(min = 2, max = 10, message = "Le nom court de la matière doit contenir entre 2 et 10 caractères")
    @Column(unique = true)
    private String nomCourtMatiere;

    @OneToMany(mappedBy = "matiere")
    private Set<ClasseMatiere> classeMatieres;

    // Getters and Setters
    public Long getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public String getNomCourtMatiere() {
        return nomCourtMatiere;
    }

    public void setNomCourtMatiere(String nomCourtMatiere) {
        this.nomCourtMatiere = nomCourtMatiere;
    }

    public Set<ClasseMatiere> getClasseMatieres() {
        return classeMatieres;
    }

    public void setClasseMatieres(Set<ClasseMatiere> classeMatieres) {
        this.classeMatieres = classeMatieres;
    }
}
