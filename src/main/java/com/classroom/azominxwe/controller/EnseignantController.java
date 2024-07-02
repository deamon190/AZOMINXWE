package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.Enseignant;
import com.classroom.azominxwe.service.EnseignantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {
    // Au cas où il y aurait des erreurs inattendues
    private static final Logger logger = Logger.getLogger(EnseignantController.class.getName());

    @Autowired
    private EnseignantService enseignantService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        return "enseignants/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Enseignant> enseignant = enseignantService.getEnseignantById(id);
        if (enseignant.isPresent()) {
            model.addAttribute("enseignant", enseignant.get());
            return "enseignants/detail";
        } else {
            model.addAttribute("erreur", "Enseignant non trouvé");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("enseignant", new Enseignant());
        return "enseignants/form";
    }

    @PostMapping("/update/{id}")
    public String updateEnseignant(@PathVariable("id") Long id, @Valid @ModelAttribute("enseignant") Enseignant enseignant,
                                   BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("enseignant", enseignant);
            return "enseignants/detail";
        }
        enseignantService.update(id, enseignant);
        redirectAttributes.addFlashAttribute("message", "Enseignant modifié avec succès !!!");
        return "redirect:/enseignants";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Enseignant enseignant, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("enseignant", enseignant);
            return "enseignants/form";
        }

        enseignantService.saveEnseignant(enseignant);
        redirectAttributes.addFlashAttribute("message", "Enseignant enregistré avec succès !!!");
        return "redirect:/enseignants";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Enseignant> enseignant = enseignantService.getEnseignantById(id);
        if (enseignant.isPresent()) {
            enseignantService.deleteEnseignant(id);
            redirectAttributes.addFlashAttribute("message", "Enseignant supprimé avec succès");
            return "redirect:/enseignants";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Enseignant non trouvé");
            return "erreur";
        }
    }

    // Gestion des exceptions donc
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }
}
