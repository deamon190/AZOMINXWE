package com.classroom.azominxwe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @NotNull(message = "La note ne peut pas être nulle")
    @DecimalMin(value = "0.0", message = "La note ne peut pas être inférieure à 0.0")
    @DecimalMax(value = "20.0", message = "La note ne peut pas être supérieure à 20.0")
    private Double note;

    @NotNull(message = "L'étudiant ne peut pas être nul")
    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @NotNull(message = "La classe-matière ne peut pas être nulle")
    @ManyToOne
    @JoinColumn(name = "classe_matiere_id", nullable = false)
    private ClasseMatiere classeMatiere;


    @ManyToOne
    @JoinColumn(name = "trimestreId", nullable = false)
    private Trimestre trimestre;

    // Getters and Setters
    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public ClasseMatiere getClasseMatiere() {
        return classeMatiere;
    }

    public void setClasseMatiere(ClasseMatiere classeMatiere) {
        this.classeMatiere = classeMatiere;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public Trimestre getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Trimestre trimestre) {
        this.trimestre = trimestre;
    }
}
