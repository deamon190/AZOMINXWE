package com.classroom.azominxwe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Eleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eleveId;
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;
    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;
    @NotBlank(message = "Le matricule ne peut pas être vide")
    @Size(min = 1, max = 20, message = "Le matricule doit contenir entre 1 et 20 caractères")
    private String matricule;

    @OneToMany(mappedBy = "eleve")
    private Set<Note> notes;

    @OneToMany(mappedBy = "eleve")
    private Set<MoyenneTrimestre> moyennesTrimestre;

    // Getters and Setters
    public Long getEleveId() {
        return eleveId;
    }

    public void setEleveId(Long eleveId) {
        this.eleveId = eleveId;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<MoyenneTrimestre> getMoyennesTrimestre() {
        return moyennesTrimestre;
    }

    public void setMoyennesTrimestre(Set<MoyenneTrimestre> moyennesTrimestre) {
        this.moyennesTrimestre = moyennesTrimestre;
    }
}