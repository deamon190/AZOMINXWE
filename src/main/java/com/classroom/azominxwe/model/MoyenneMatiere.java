package com.classroom.azominxwe.model;

import jakarta.persistence.*;

@Entity
public class MoyenneMatiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moyenneMatiereId;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "classe_matiere_id")
    private ClasseMatiere classeMatiere;

    private Double moyenne;

    @ManyToOne
    @JoinColumn(name = "trimestre_id")
    private Trimestre trimestre;

    // Getters and setters
    public Long getMoyenneMatiereId() {
        return moyenneMatiereId;
    }

    public void setMoyenneMatiereId(Long moyenneMatiereId) {
        this.moyenneMatiereId = moyenneMatiereId;
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

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }

    public Trimestre getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Trimestre trimestre) {
        this.trimestre = trimestre;
    }
}
