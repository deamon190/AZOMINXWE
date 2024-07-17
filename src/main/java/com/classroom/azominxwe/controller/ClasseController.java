package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.AnneeAcademique;
import com.classroom.azominxwe.model.Classe;
import com.classroom.azominxwe.service.AnneeAcademiqueService;
import com.classroom.azominxwe.service.ClasseService;
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
@RequestMapping("/classes")
public class ClasseController {
    // Au cas où il y aurait des erreurs inattendues
    private static final Logger logger = Logger.getLogger(ClasseController.class.getName());

    @Autowired
    private ClasseService classeService;

    @Autowired
    private AnneeAcademiqueService anneeAcademiqueService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", classeService.getClassesByAnneeAcademiqueActive());
        return "classes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Classe> classe = classeService.getClasseById(id);
        AnneeAcademique anneesAcademiques = anneeAcademiqueService.getActiveAnneeAcademique();
        if (classe.isPresent()) {
            model.addAttribute("classe", classe.get());
            model.addAttribute("anneesAcademiques", anneesAcademiques);
            return "classes/detail";
        } else {
            model.addAttribute("erreur", "Classe non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("classe", new Classe());
        model.addAttribute("anneesAcademiques", anneeAcademiqueService.getActiveAnneeAcademique());
        return "classes/form";
    }

    @PostMapping("/update/{id}")
    public String updateClasse(@PathVariable("id") Long id, @Valid @ModelAttribute("classe") Classe classe,
                               BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("classe", classe);
            model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
            return "classes/detail";
        }
        classeService.update(id, classe);
        redirectAttributes.addFlashAttribute("message", "Classe modifiée avec succès !!!");
        return "redirect:/classes";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Classe classe, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("classe", classe);
            model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
            return "classes/form";
        }

        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueService.getAnneeAcademiqueById(classe.getAnneeAcademique().getAnneeAcademiqueId());
        if (anneeAcademique.isPresent()) {
            classe.setAnneeAcademique(anneeAcademique.get());
            classeService.saveClasse(classe);
            redirectAttributes.addFlashAttribute("message", "Classe enregistrée avec succès !!!");
            return "redirect:/classes";
        } else {
            model.addAttribute("classe", classe);
            model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
            model.addAttribute("erreur", "Année académique non trouvée");
            return "classes/form";
        }
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Classe> classe = classeService.getClasseById(id);
        if (classe.isPresent()) {
            classeService.deleteClasse(id);
            redirectAttributes.addFlashAttribute("message", "Classe supprimée avec succès");
            return "redirect:/classes";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Classe non trouvée");
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
