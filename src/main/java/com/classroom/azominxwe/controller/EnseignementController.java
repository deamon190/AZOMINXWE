package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.ClasseMatiere;
import com.classroom.azominxwe.model.Enseignant;
import com.classroom.azominxwe.model.Enseignement;
import com.classroom.azominxwe.service.AnneeAcademiqueService;
import com.classroom.azominxwe.service.ClasseMatiereService;
import com.classroom.azominxwe.service.EnseignantService;
import com.classroom.azominxwe.service.EnseignementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/enseignements")
public class EnseignementController {
    private static final Logger logger = Logger.getLogger(EnseignementController.class.getName());

    @Autowired
    private EnseignementService enseignementService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private ClasseMatiereService classeMatiereService;

    @Autowired
    private AnneeAcademiqueService anneeAcademiqueService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enseignements", enseignementService.Enseignements_anneeactive());
        return "enseignements/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Enseignement> enseignement = enseignementService.getEnseignementById(id);
        if (enseignement.isPresent()) {
            model.addAttribute("enseignement", enseignement.get());
            model.addAttribute("enseignants", getEnseignantNomPrenomCombines());
            model.addAttribute("classeMatieres", getUnassignedClasseMatiereCombines());
            return "enseignements/detail";
        } else {
            model.addAttribute("erreur", "Enseignement non trouvé");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("enseignement", new Enseignement());
        model.addAttribute("enseignants", getEnseignantNomPrenomCombines());
        model.addAttribute("classeMatieres", getUnassignedClasseMatiereCombines());
        return "enseignements/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Enseignement enseignement, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("enseignants", getEnseignantNomPrenomCombines());
            model.addAttribute("classeMatieres", getUnassignedClasseMatiereCombines());
            return "enseignements/form";
        }


        if (enseignement.getEnseignant() == null || enseignement.getEnseignant().getEnseignantId() == null ||
                enseignement.getClasseMatiere() == null || enseignement.getClasseMatiere().getClasseMatiereId() == null) {
            model.addAttribute("erreur", "Enseignant ou Classe Matière non sélectionné");
            model.addAttribute("enseignants", getEnseignantNomPrenomCombines());
            model.addAttribute("classeMatieres", getUnassignedClasseMatiereCombines());
            return "enseignements/form";
        }

        // Charger les entités associées depuis la base de données
        Optional<Enseignant> enseignantOpt = enseignantService.getEnseignantById(enseignement.getEnseignant().getEnseignantId());
        Optional<ClasseMatiere> classeMatiereOpt = classeMatiereService.getClasseMatiereById(enseignement.getClasseMatiere().getClasseMatiereId());

        if (enseignantOpt.isPresent() && classeMatiereOpt.isPresent()) {
            enseignement.setEnseignant(enseignantOpt.get());
            enseignement.setClasseMatiere(classeMatiereOpt.get());
            enseignement.setAnneeAcademique(anneeAcademiqueService.getActiveAnneeAcademique());
            enseignementService.saveEnseignement(enseignement);
            redirectAttributes.addFlashAttribute("message", "Attribution de cours effectué avec succès !!!");
        } else {
            model.addAttribute("erreur", "Enseignant ou Classe Matière non trouvé");
            model.addAttribute("enseignants", getEnseignantNomPrenomCombines());
            model.addAttribute("classeMatieres", getUnassignedClasseMatiereCombines());
            return "enseignements/form";
        }
        return "redirect:/enseignements";
    }



    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Enseignement enseignement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            model.addAttribute("classeMatieres", classeMatiereService.getAllClasseMatieres());
            return "enseignements/form";
        }
        enseignement.setAnneeAcademique(anneeAcademiqueService.getActiveAnneeAcademique());
        enseignementService.updateEnseignement(id, enseignement);
        return "redirect:/enseignements";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        Optional<Enseignement> enseignement = enseignementService.getEnseignementById(id);
        if (enseignement.isPresent()) {
            enseignementService.deleteEnseignement(id);
            model.addAttribute("message", "Enseignement supprimé avec succès !!!");
            return "redirect:/enseignements";
        } else {
            model.addAttribute("erreur", "Enseignement non trouvé");
            return "erreur";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }

    private List<String[]> getUnassignedClasseMatiereCombines() {
        return classeMatiereService.getUnassignedClassesByAnneeAcademiqueActive().stream()
                .map(classeMatiere -> new String[] {
                        classeMatiere.getClasseMatiereId().toString(),
                        classeMatiere.getClasse().getNomClasse() + " - " + classeMatiere.getMatiere().getNomCourtMatiere() })
                .collect(Collectors.toList());
    }

    private List<String[]> getEnseignantNomPrenomCombines() {
        return enseignantService.getAllEnseignants().stream()
                .map(enseignant -> new String[] {
                        enseignant.getEnseignantId().toString(),
                        enseignant.getNom() + " " + enseignant.getPrenom() })
                .collect(Collectors.toList());
    }

}
