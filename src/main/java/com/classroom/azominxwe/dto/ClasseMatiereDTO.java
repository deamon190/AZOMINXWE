package com.classroom.azominxwe.dto;

public class ClasseMatiereDTO {
    private Long classeMatiereId;
    private String nomClasse;
    private String nomMatiere;

    // Constructeurs, getters et setters

    public ClasseMatiereDTO(Long classeMatiereId, String nomClasse, String nomMatiere) {
        this.classeMatiereId = classeMatiereId;
        this.nomClasse = nomClasse;
        this.nomMatiere = nomMatiere;
    }

    // Getters et setters
    public Long getClasseMatiereId() {
        return classeMatiereId;
    }

    public void setClasseMatiereId(Long classeMatiereId) {
        this.classeMatiereId = classeMatiereId;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }
}
