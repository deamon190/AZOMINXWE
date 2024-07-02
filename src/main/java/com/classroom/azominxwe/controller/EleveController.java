package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.model.MoyenneTrimestre;
import com.classroom.azominxwe.model.Trimestre;
import com.classroom.azominxwe.repository.EleveRepository;
import com.classroom.azominxwe.repository.MoyenneTrimestreRepository;
import com.classroom.azominxwe.repository.TrimestreRepository;
import com.classroom.azominxwe.service.EleveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@RequestMapping("/eleves")
public class EleveController {
    private static final Logger logger = Logger.getLogger(EleveController.class.getName());

    @Autowired
    private EleveService eleveService;
    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private MoyenneTrimestreRepository moyenneTrimestreRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @GetMapping
    public String list(Model model) {
        List<Eleve> eleves = eleveRepository.findAll();
        Trimestre trimestreActif = trimestreRepository.findByActifTrue();

        for (Eleve eleve : eleves) {
            Set<MoyenneTrimestre> moyenneTrimestre = moyenneTrimestreRepository.findByEleveAndTrimestre(eleve, trimestreActif);
            if (moyenneTrimestre != null) {
                eleve.setMoyennesTrimestre(moyenneTrimestre);
            } else {
                eleve.setMoyennesTrimestre(null);
            }
        }

        model.addAttribute("eleves", eleves);
        return "eleves/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Eleve> eleve = eleveService.getEleveById(id);
        if (eleve.isPresent()) {
            model.addAttribute("eleve", eleve.get());
            return "eleves/detail";
        } else {
            model.addAttribute("erreur", "Éleve non trouvé");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("eleve", new Eleve());
        return "eleves/form";
    }

    @PostMapping("/update/{id}")
    public String updateEleve(@PathVariable("id") Long id, @Valid @ModelAttribute("eleve") Eleve eleve,
                                 BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("eleve", eleve);
            return "eleves/detail";
        }

        Eleve elv=eleveService.getEleveByMatricule(eleve.getMatricule());
        if (elv!=null && elv.getEleveId()==id)
        {
            eleveService.update(id, eleve);
            redirectAttributes.addFlashAttribute("message", "Éleve modifié avec succès !!!");
            return "redirect:/eleves";
        }
        else
        {   model.addAttribute("eleve", eleve);
            model.addAttribute("erreur", "Ce Matricule existe déjà !!!");
            return "eleves/detail";
        }


    }

    @PostMapping
    public String save(@Valid @ModelAttribute Eleve eleve, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("eleve", eleve);
            return "eleves/form";
        }

        if (!eleveService.checkbyMatricule(eleve.getMatricule()))
        {
            eleveService.saveEleve(eleve);
            redirectAttributes.addFlashAttribute("message", "Éleve enregistré avec succès !!!");
            return "redirect:/eleves";
        }
        else
        {   model.addAttribute("eleve", eleve);
            model.addAttribute("erreur", "Ce Matricule existe déjà !!!");
            return "eleves/form";
        }

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Eleve> eleve = eleveService.getEleveById(id);
        if (eleve.isPresent()) {
            eleveService.deleteEleve(id);
            redirectAttributes.addFlashAttribute("message", "Éleve supprimé avec succès");
            return "redirect:/eleves";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Éleve non trouvé");
            return "erreur";
        }
    }

    // Gestion des exceptions
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }
}
