package com.classroom.azominxwe.model;
import com.classroom.azominxwe.validation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;
@Entity
@ValidDateRange
public class Trimestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trimestreId;
    @NotBlank(message = "Le nom du trimestre ne peut pas être vide")
    @Size(min = 2, max = 40, message = "Le nom du Trimestre doit être entre 2 et 40 caractères")
    @Pattern(regexp = "^Trimestre [1-4]$", message = "Le nom du trimestre doit être sous la forme 'Trimestre [1-4]'")
    private String nom;

    @NotNull(message = "La date de début ne peut pas être nulle")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDebut;

    @NotNull(message = "La date de fin ne peut pas être nulle")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;

    @NotNull
    private Boolean actif = false;  // Initialisation par défaut

    @ManyToOne
    @JoinColumn(name = "annee_academique_id")
    private AnneeAcademique anneeAcademique;

    @OneToMany(mappedBy = "trimestre")
    private Set<Note> notes;

    @OneToMany(mappedBy = "trimestre")
    private Set<MoyenneTrimestre> moyennesTrimestre;

    // Getters and Setters
    public Long getTrimestreId() {
        return trimestreId;
    }

    public void setTrimestreId(Long trimestreId) {
        this.trimestreId = trimestreId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}