package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.MoyenneTrimestre;
import com.classroom.azominxwe.service.MoyenneTrimestreService;
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
@RequestMapping("/moyennesTrimestre")
public class MoyenneTrimestreController {
    private static final Logger logger = Logger.getLogger(MoyenneTrimestreController.class.getName());

    @Autowired
    private MoyenneTrimestreService moyenneTrimestreService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("moyennesTrimestre", moyenneTrimestreService.getAllMoyennesTrimestre());
        return "moyennesTrimestre/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<MoyenneTrimestre> moyenneTrimestre = moyenneTrimestreService.getMoyenneTrimestreById(id);
        if (moyenneTrimestre.isPresent()) {
            model.addAttribute("moyenneTrimestre", moyenneTrimestre.get());
            return "moyennesTrimestre/detail";
        } else {
            model.addAttribute("erreur", "Moyenne de trimestre non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("moyenneTrimestre", new MoyenneTrimestre());
        return "moyennesTrimestre/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute MoyenneTrimestre moyenneTrimestre, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("moyenneTrimestre", moyenneTrimestre);
            return "moyennesTrimestre/form";
        }
        moyenneTrimestreService.saveMoyenneTrimestre(moyenneTrimestre);
        redirectAttributes.addFlashAttribute("message", "Moyenne calculée avec succès pour le trimestre !!!");
        return "redirect:/moyennesTrimestre";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        Optional<MoyenneTrimestre> moyenneTrimestre = moyenneTrimestreService.getMoyenneTrimestreById(id);
        if (moyenneTrimestre.isPresent()) {
            moyenneTrimestreService.deleteMoyenneTrimestre(id);
            return "redirect:/moyennesTrimestre";
        } else {
            model.addAttribute("erreur", "Moyenne de trimestre non trouvée");
            return "erreur";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }
}
