package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.AnneeAcademique;
import com.classroom.azominxwe.service.AnneeAcademiqueService;
import com.classroom.azominxwe.service.StatistiqueService;
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
@RequestMapping("/anneesAcademiques")
public class AnneeAcademiqueController {
    private static final Logger logger = Logger.getLogger(AnneeAcademiqueController.class.getName());

    @Autowired
    private StatistiqueService statistiqueService;

    @Autowired
    private AnneeAcademiqueService anneeAcademiqueService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
        return "anneesAcademiques/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueService.getAnneeAcademiqueById(id);
        if (anneeAcademique.isPresent()) {
            model.addAttribute("anneeAcademique", anneeAcademique.get());
            return "anneesAcademiques/detail";
        } else {
            model.addAttribute("erreur", "Année académique non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("anneeAcademique", new AnneeAcademique());
        return "anneesAcademiques/form";
    }

    @PostMapping("/update/{id}")
    public String updateAnneeAcademique(@PathVariable("id") Long id, @Valid @ModelAttribute("anneeAcademique") AnneeAcademique anneeAcademique,
                                        BindingResult result,Model model,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("anneeAcademique", anneeAcademique);
            return "anneesAcademiques/detail";
        }
        anneeAcademiqueService.update(id, anneeAcademique);
        redirectAttributes.addFlashAttribute("message", "Année académique modifiée avec succès !!!");
        return "redirect:/anneesAcademiques";
    }

    @PostMapping("/active_me/{id}")
    public String activateAnneeAcademique(@PathVariable("id") Long id,  RedirectAttributes redirectAttributes) {
        anneeAcademiqueService.activateAnneeAcademique(id);
        redirectAttributes.addFlashAttribute("message", "Année académique activée avec succès !!!");
        return "redirect:/anneesAcademiques";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute AnneeAcademique anneeAcademique, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
       String msg_text="Année académique enregistrée avec succès !!!";
        if (result.hasErrors()) {
            model.addAttribute("anneeAcademique", anneeAcademique);
            return "anneesAcademiques/form";
        }
        if (statistiqueService.getNombreAnneeAcademiques()==0)
        {
            anneeAcademique.setActif(true);
            msg_text="Année académique enregistrée avec succès et activée car unique !!!";
        }
        else
        {
            anneeAcademique.setActif(false);
        }

        anneeAcademiqueService.saveAnneeAcademique(anneeAcademique);
        redirectAttributes.addFlashAttribute("message", msg_text);
        return "redirect:/anneesAcademiques";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueService.getAnneeAcademiqueById(id);
        if (anneeAcademique.isPresent()) {
            //vérifier si il est actif
            if (statistiqueService.isAnneeAcademiqueActive(id))
            {
                redirectAttributes.addFlashAttribute("erreur", "Seule une année académique non activée peut être supprimée !!!");
                return "redirect:/anneesAcademiques";
            }
            anneeAcademiqueService.deleteAnneeAcademique(id);
            redirectAttributes.addFlashAttribute("message", "Année académique supprimée avec succès !!!");
            return "redirect:/anneesAcademiques";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Année académique non trouvée !!!");
            return "anneesAcademiques";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }
}
