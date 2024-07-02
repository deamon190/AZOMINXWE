package com.classroom.azominxwe.model;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;

@Entity
public class Enseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enseignementId;

    @NotNull(message = "L'enseignant ne peut pas être nul")
    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @NotNull(message = "La classe-matière ne peut pas être nulle")
    @ManyToOne
    @JoinColumn(name = "classe_matiere_id")
    private ClasseMatiere classeMatiere;

    @ManyToOne
    @JoinColumn(name = "annee_academique_id")
    private AnneeAcademique anneeAcademique;

    // Getters and Setters
    public Long getEnseignementId() {
        return enseignementId;
    }

    public void setEnseignementId(Long enseignementId) {
        this.enseignementId = enseignementId;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public ClasseMatiere getClasseMatiere() {
        return classeMatiere;
    }

    public void setClasseMatiere(ClasseMatiere classeMatiere) {
        this.classeMatiere = classeMatiere;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    // Méthodes auxiliaires pour l'ID de l'enseignant
    public Long getEnseignantId() {
        return enseignant != null ? enseignant.getEnseignantId() : null;
    }

    public void setEnseignantId(Long enseignantId) {
        if (this.enseignant == null) {
            this.enseignant = new Enseignant();
        }
        this.enseignant.setEnseignantId(enseignantId);
    }

    // Méthodes auxiliaires pour l'ID de la classe-matière
    public Long getClasseMatiereId() {
        return classeMatiere != null ? classeMatiere.getClasseMatiereId() : null;
    }

    public void setClasseMatiereId(Long classeMatiereId) {
        if (this.classeMatiere == null) {
            this.classeMatiere = new ClasseMatiere();
        }
        this.classeMatiere.setClasseMatiereId(classeMatiereId);
    }

    // Méthodes auxiliaires pour l'ID de l'année académique
    public Long getAnneeAcademiqueId() {
        return anneeAcademique != null ? anneeAcademique.getAnneeAcademiqueId() : null;
    }

    public void setAnneeAcademiqueId(Long anneeAcademiqueId) {
        if (this.anneeAcademique == null) {
            this.anneeAcademique = new AnneeAcademique();
        }
        this.anneeAcademique.setAnneeAcademiqueId(anneeAcademiqueId);
    }
}
