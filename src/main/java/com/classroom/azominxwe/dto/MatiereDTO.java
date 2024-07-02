package com.classroom.azominxwe.dto;

public class MatiereDTO {
    private Long matiereId;
    private String nomMatiere;
    private String nomCourtMatiere;

    // Constructeurs, getters et setters

    public MatiereDTO(Long matiereId, String nomMatiere, String nomCourtMatiere) {
        this.matiereId = matiereId;
        this.nomMatiere = nomMatiere;
        this.nomCourtMatiere = nomCourtMatiere;
    }

    // Getters et setters
    public Long getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
    }

    public String getnomMatiere() {
        return nomMatiere;
    }

    public void setnomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public String getnomCourtMatiere() {
        return nomCourtMatiere;
    }

    public void setnomCourtMatiere(String nomMatiere) {
        this.nomCourtMatiere = nomCourtMatiere;
    }
}
