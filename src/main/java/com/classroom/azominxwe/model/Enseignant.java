package com.classroom.azominxwe.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Enseignant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enseignantId;
    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être valide")
    private String email;


    @OneToMany(mappedBy = "enseignant")
    private Set<Enseignement> enseignements;

    // Getters and Setters
    public Long getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(Long enseignantId) {
        this.enseignantId = enseignantId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Enseignement> getEnseignements() {
        return enseignements;
    }

    public void setEnseignements(Set<Enseignement> enseignements) {
        this.enseignements = enseignements;
    }
}