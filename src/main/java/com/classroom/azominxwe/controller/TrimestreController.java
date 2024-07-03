package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.AnneeAcademique;
import com.classroom.azominxwe.model.Trimestre;
import com.classroom.azominxwe.service.AnneeAcademiqueService;
import com.classroom.azominxwe.service.MoyenneTrimestreService;
import com.classroom.azominxwe.service.StatistiqueService;
import com.classroom.azominxwe.service.TrimestreService;
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
import java.util.stream.Stream;

@Controller
@RequestMapping("/trimestres")
public class TrimestreController {
    private static final Logger logger = Logger.getLogger(TrimestreController.class.getName());
    List<String> availableTrimestres;

    @Autowired
    private StatistiqueService statistiqueService;
    @Autowired
    private TrimestreService trimestreService;

    @Autowired
    private AnneeAcademiqueService anneeAcademiqueService;

    @Autowired
    private MoyenneTrimestreService moyenneTrimestreService;


    @GetMapping
    public String list(Model model) {
        long tmp_id=anneeAcademiqueService.getActiveAnneeAcademique().getAnneeAcademiqueId();
        model.addAttribute("trimestres", trimestreService.getTrimestresByAnneeAcademique(tmp_id));
        return "trimestres/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Trimestre> trimestre = trimestreService.getTrimestreById(id);
        List<AnneeAcademique> anneesAcademiques = anneeAcademiqueService.getAllAnneesAcademiques();
        if (trimestre.isPresent()) {
            model.addAttribute("trimestre", trimestre.get());
            model.addAttribute("anneesAcademiques", anneesAcademiques);
            return "trimestres/detail";
        } else {
            model.addAttribute("erreur", "Trimestre non trouvé");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        List<String> allTrimestres = Stream.of("Trimestre 1", "Trimestre 2", "Trimestre 3").collect(Collectors.toList());
        List<Trimestre> existingTrimestres = trimestreService.getTrimestresByAnneeAcademiqueActive();
        availableTrimestres = allTrimestres.stream()
                .filter(t -> existingTrimestres.stream().noneMatch(et -> et.getNom().equals(t)))
                .collect(Collectors.toList());

        model.addAttribute("availableTrimestres", availableTrimestres);
        model.addAttribute("trimestre", new Trimestre());
        model.addAttribute("anneesAcademiques", anneeAcademiqueService.getActiveAnneeAcademique());
        return "trimestres/form";
    }

    @PostMapping("/update/{id}")
    public String updateTrimestre(@PathVariable("id") Long id, @Valid @ModelAttribute("trimestre") Trimestre trimestre,
                                  BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("trimestre", trimestre);
            model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
            return "trimestres/detail";
        }
        trimestreService.update(id, trimestre);
        redirectAttributes.addFlashAttribute("message", "Trimestre modifié avec succès !!!");
        return "redirect:/trimestres";
        //return String.valueOf(trimestre.getAnneeAcademique().getAnneeAcademiqueId());
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Trimestre trimestre, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        String msg_text="Trimestre enregistré avec succès !!!";
        model.addAttribute("availableTrimestres", availableTrimestres);

        if (result.hasErrors()) {
            model.addAttribute("trimestre", trimestre);
            model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
            return "trimestres/form";
        }
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueService.getAnneeAcademiqueById(trimestre.getAnneeAcademique().getAnneeAcademiqueId());
        if (anneeAcademique.isPresent()) {
            trimestre.setAnneeAcademique(anneeAcademique.get());
            if (statistiqueService.getNombreTrimestres()==0)
            {
                trimestre.setActif(true);
                msg_text="Trimestre enregistré avec succès et activé car unique !!!";
            }else
            {
                trimestre.setActif(false);
            }

            trimestreService.saveTrimestre(trimestre);
            redirectAttributes.addFlashAttribute("message", msg_text);
            return "redirect:/trimestres";
        } else {
            model.addAttribute("trimestre", trimestre);
            model.addAttribute("anneesAcademiques", anneeAcademiqueService.getAllAnneesAcademiques());
            model.addAttribute("erreur", "Année académique non trouvée");
            return "trimestres/form";
        }

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Trimestre> trimestre = trimestreService.getTrimestreById(id);
        if (trimestre.isPresent()) {
            trimestreService.deleteTrimestre(id);
            redirectAttributes.addFlashAttribute("message", "Trimestre supprimé avec succès");
            return "redirect:/trimestres";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Trimestre non trouvé");
            return "erreur";
        }
    }

    @PostMapping("/active_me/{id}")
    public String activateTrimestre(@PathVariable("id") Long id,  RedirectAttributes redirectAttributes) {
        trimestreService.activateTrimestre(id);
        redirectAttributes.addFlashAttribute("message", "Trimestre activé avec succès !!!");
        return "redirect:/trimestres";
    }

    @PostMapping("/calculer_moyenne/{id}")
    public String calculateAverages(@PathVariable Long id, Model model,RedirectAttributes redirectAttributes) {
        moyenneTrimestreService.calculerMoyenneParTrimestre(id);
        redirectAttributes.addFlashAttribute("message","Les moyennes ont été calculées avec succès pour le trimestre ID: " + id);
        return "redirect:/trimestres";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }


}
