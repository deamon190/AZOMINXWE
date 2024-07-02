package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.dto.ClasseMatiereDTO;
import com.classroom.azominxwe.model.ClasseMatiere;
import com.classroom.azominxwe.service.ClasseMatiereService;
import com.classroom.azominxwe.service.ClasseService;
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
@RequestMapping("/classe-matiere")
public class ClasseMatiereController {
    private static final Logger logger = Logger.getLogger(ClasseMatiereController.class.getName());

    @Autowired
    private ClasseMatiereService classeMatiereService;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private MatiereService matiereService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classeMatieres", classeMatiereService.getClassesByAnneeAcademiqueActive());
        return "classesMatieres/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<ClasseMatiere> classeMatiere = classeMatiereService.getClasseMatiereById(id);
        if (classeMatiere.isPresent()) {
            model.addAttribute("classMatiere", classeMatiere.get());
            model.addAttribute("classeMatiere", classeMatiere.get());
            model.addAttribute("classes", classeService.getClassesByAnneeAcademiqueActive());
            model.addAttribute("matieres", matiereService.getAllMatieres());

            return "classesMatieres/detail";
        } else {
            model.addAttribute("erreur", "Classe-Matière non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("classeMatiere", new ClasseMatiere());
        model.addAttribute("classes", classeService.getClassesByAnneeAcademiqueActive());
        model.addAttribute("matieres", matiereService.getAllMatieres());
        return "classesMatieres/form";
    }
    @PostMapping("/update/{id}")
    public String updateClasseMatiere(@PathVariable("id") Long id, @Valid @ModelAttribute("classeMatiere") ClasseMatiere classeMatiere,
                                      BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Optional<ClasseMatiere> classMatiere = classeMatiereService.getClasseMatiereById(id);

        if (result.hasErrors()) {
            model.addAttribute("classMatiere", classMatiere.get());
            model.addAttribute("classeMatiere", classeMatiere);
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("matieres", matiereService.getAllMatieres());
            return "classesMatieres/detail";
        }

        Optional<ClasseMatiere> existingClasseMatiere = classeMatiereService.findByClasseAndMatiereAndAnneeAcademiqueActive(
                classeMatiere.getClasse().getClasseId(), classeMatiere.getMatiere().getMatiereId());

        if (existingClasseMatiere.isPresent() && !existingClasseMatiere.get().getClasseMatiereId().equals(id)) {
            redirectAttributes.addFlashAttribute("erreur", "Cette combinaison classe-matière existe déjà pour l'année académique active.");
            return "redirect:/classe-matiere/" + id;
        }

        classeMatiereService.update(id, classeMatiere);
        redirectAttributes.addFlashAttribute("message", "Classe-Matière modifiée avec succès !!!");
        return "redirect:/classe-matiere";
    }



    @PostMapping
    public String save(@Valid @ModelAttribute ClasseMatiere classeMatiere, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("classeMatiere", classeMatiere);
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("matieres", matiereService.getAllMatieres());
            return "classesMatieres/form";
        }

        Optional<ClasseMatiere> existingClasseMatiere = classeMatiereService.findByClasseAndMatiereAndAnneeAcademiqueActive(
                classeMatiere.getClasse().getClasseId(), classeMatiere.getMatiere().getMatiereId());

        if (existingClasseMatiere.isPresent()) {
            redirectAttributes.addFlashAttribute("erreur", "Cette combinaison classe-matière existe déjà pour l'année académique active.");
            return "redirect:/classe-matiere/new";
        }

        classeMatiereService.saveMatiereClasse(classeMatiere);
        redirectAttributes.addFlashAttribute("message", "Classe-Matière enregistrée avec succès !!!");
        return "redirect:/classe-matiere";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<ClasseMatiere> classeMatiere = classeMatiereService.getClasseMatiereById(id);
        if (classeMatiere.isPresent()) {
            classeMatiereService.deleteMatiereClasse(id);
            redirectAttributes.addFlashAttribute("message", "Classe-Matière supprimée avec succès");
            return "redirect:/classe-matiere";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Classe-Matière non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/unassignedClasseMatieres/{eleveId}")
    @ResponseBody
    public List<ClasseMatiereDTO> getUnassignedClasseMatieres(@PathVariable Long eleveId) {
        return classeMatiereService.getClasseMatieresForEleve(eleveId);
    }


    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }
}
