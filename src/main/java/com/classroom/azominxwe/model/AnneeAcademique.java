package com.classroom.azominxwe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AnneeAcademique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long anneeAcademiqueId;

    @NotBlank(message = "L''année académique ne peut pas être vide")
    @Pattern(regexp = "\\d{4}-\\d{4}", message = "L''année académique doit être au format 'YYYY-YYYY'")
    private String annee;

    @NotNull
    private Boolean actif = false;  // Initialisation par défaut

    // Getters and Setters
    public Long getAnneeAcademiqueId() {
        return anneeAcademiqueId;
    }

    public void setAnneeAcademiqueId(Long anneeAcademiqueId) {
        this.anneeAcademiqueId = anneeAcademiqueId;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
    public Boolean getActif() {
        return actif;
    }
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}