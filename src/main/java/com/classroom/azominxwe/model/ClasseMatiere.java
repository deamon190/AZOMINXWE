package com.classroom.azominxwe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ClasseMatiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classeMatiereId;



    @Min(value = 1, message = "Le coefficient ne peut pas être inférieur à 1.")
    @Max(value = 10, message = "Le coefficient ne peut pas être supérieur à 10.")
    private int coefficient = 1;

    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    // Getters and Setters

    public Long getClasseMatiereId() {
        return classeMatiereId;
    }

    public void setClasseMatiereId(Long classeMatiereId) {

        this.classeMatiereId = classeMatiereId;
    }
    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
}
