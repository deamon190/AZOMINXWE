package com.classroom.azominxwe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classeId;

    @NotBlank(message = "Le nom de la classe ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la classe doit contenir entre 2 et 100 caractères")
    private String nomClasse;


    @ManyToOne
    @JoinColumn(name = "annee_academique_id")
    private AnneeAcademique anneeAcademique;

    @OneToMany(mappedBy = "classe")
    private Set<MoyenneTrimestre> moyennesTrimestre;

    @OneToMany(mappedBy = "classe")
    private Set<ClasseMatiere> classeMatieres;

    // Getters and Setters
    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Set<MoyenneTrimestre> getMoyennesTrimestre() {
        return moyennesTrimestre;
    }

    public void setMoyennesTrimestre(Set<MoyenneTrimestre> moyennesTrimestre) {
        this.moyennesTrimestre = moyennesTrimestre;
    }

    public Set<ClasseMatiere> getClasseMatieres() {
        return classeMatieres;
    }

    public void setClasseMatieres(Set<ClasseMatiere> classeMatieres) {
        this.classeMatieres = classeMatieres;
    }
}
