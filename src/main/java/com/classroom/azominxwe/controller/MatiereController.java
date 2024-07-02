package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.dto.MatiereDTO;
import com.classroom.azominxwe.model.Matiere;
import com.classroom.azominxwe.repository.MatiereRepository;
import com.classroom.azominxwe.service.MatiereService;
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

@Controller
@RequestMapping("/matieres")
public class MatiereController {
    // Au cas où il y aurait des erreurs inattendues
    private static final Logger logger = Logger.getLogger(MatiereController.class.getName());

    @Autowired
    private MatiereService matiereService;

    @Autowired
    private MatiereRepository matiereRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("matieres", matiereService.getAllMatieres());
        return "matieres/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Matiere> matiere = matiereService.getMatiereById(id);
        if (matiere.isPresent()) {
            model.addAttribute("matiere", matiere.get());
            return "matieres/detail";
        } else {
            model.addAttribute("erreur", "Matière non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("matiere", new Matiere());
        return "matieres/form";
    }

    @PostMapping("/update/{id}")
    public String updateMatiere(@PathVariable("id") Long id, @Valid @ModelAttribute("matiere") Matiere matiere,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("matiere", matiere);
            return "matieres/detail";
        }

        if (matiereRepository.existsByNomMatiere(matiere.getNomMatiere())) {
            model.addAttribute("erreur", "Le nom de la matière existe déjà !!!");
            model.addAttribute("matiere", matiere);
            return "matieres/detail";
        }
        if (matiereRepository.existsByNomCourtMatiere(matiere.getNomCourtMatiere())) {
            model.addAttribute("erreur", "Le nom court de la matière existe déjà !!!");
            model.addAttribute("matiere", matiere);
            return "matieres/detail";
        }

        matiereService.update(id, matiere);
        redirectAttributes.addFlashAttribute("message", "Matière modifiée avec succès !!!");
        return "redirect:/matieres";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Matiere matiere, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("matiere", matiere);
            return "matieres/form";
        }

        if (matiereRepository.existsByNomMatiere(matiere.getNomMatiere())) {
            model.addAttribute("erreur", "Le nom de la matière existe déjà !!!");
            model.addAttribute("matiere", matiere);
            return "matieres/form";
        }
        if (matiereRepository.existsByNomCourtMatiere(matiere.getNomCourtMatiere())) {

            model.addAttribute("erreur", "Le nom court de la matière existe déjà !!!");
            model.addAttribute("matiere", matiere);
            return "matieres/form";
        }

        matiereService.saveMatiere(matiere);
        redirectAttributes.addFlashAttribute("message", "Matière enregistrée avec succès !!!");
        return "redirect:/matieres";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Matiere> matiere = matiereService.getMatiereById(id);
        if (matiere.isPresent()) {
            matiereService.deleteMatiere(id);
            redirectAttributes.addFlashAttribute("message", "Matière supprimée avec succès");
            return "redirect:/matieres";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Matière non trouvée");
            return "redirect:/erreur";
        }
    }

    @GetMapping("/get-matieres-by-classe/{classeId}")
    @ResponseBody
    public List<MatiereDTO> getUnassignedMatieres(@PathVariable Long classeId) {
        return matiereService.getMatieresForClasse(classeId);
    }



    // Gestion des exceptions donc
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }
}
