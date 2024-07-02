package com.classroom.azominxwe.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;

@Entity
public class MoyenneTrimestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moyenneId;

    @NotNull(message = "L'étudiant ne peut pas être nul")
    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @NotNull(message = "La classe ne peut pas être nulle")
    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @NotNull(message = "Le trimestre ne peut pas être nul")
    @ManyToOne
    @JoinColumn(name = "trimestre_id")
    private Trimestre trimestre;

    @NotNull(message = "La moyenne ne peut pas être nulle")
    @DecimalMin(value = "0.0", message = "La moyenne ne peut pas être inférieure à 0.0")
    @DecimalMax(value = "20.0", message = "La moyenne ne peut pas être supérieure à 20.0")
    private Double moyenne;

    // Getters and Setters
    public Long getMoyenneId() {
        return moyenneId;
    }

    public void setMoyenneId(Long moyenneId) {
        this.moyenneId = moyenneId;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Trimestre getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Trimestre trimestre) {
        this.trimestre = trimestre;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }
}