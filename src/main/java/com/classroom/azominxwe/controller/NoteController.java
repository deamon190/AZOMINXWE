package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.ClasseMatiere;
import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.model.Note;
import com.classroom.azominxwe.service.ClasseMatiereService;
import com.classroom.azominxwe.service.EleveService;
import com.classroom.azominxwe.service.NoteService;
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

@Controller
@RequestMapping("/notes")
public class NoteController {
    private static final Logger logger = Logger.getLogger(NoteController.class.getName());

    @Autowired
    private NoteService noteService;

    @Autowired
    private EleveService eleveService;

    @Autowired
    private ClasseMatiereService classeMatiereService;

    @Autowired
    private TrimestreService trimestreService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notes", noteService.getNotesParTrimestreActif());
        return "notes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent()) {
            model.addAttribute("note", note.get());
            model.addAttribute("eleves", getEleveNomPrenomCombines());
            model.addAttribute("classeMatieres", classeMatiereService.getClasseMatieresForEleve(note.get().getEleve().getEleveId()));
            return "notes/detail";
        } else {
            model.addAttribute("erreur", "Note non trouvée");
            return "erreur";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("notes", new Note());
        model.addAttribute("eleves", getEleveNomPrenomCombines());
        model.addAttribute("classeMatieres", getClasseMatiereCombines());
        model.addAttribute("trimestres", trimestreService.getActiveTrimestre());
        return "notes/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("note") Note note, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("eleves", getEleveNomPrenomCombines());
            model.addAttribute("classeMatieres", getClasseMatiereCombines());
            return "notes/form";
        }

        // Charger les entités associées depuis la base de données
        Optional<Eleve> eleveOpt = eleveService.getEleveById(note.getEleve().getEleveId());
        Optional<ClasseMatiere> classeMatiereOpt = classeMatiereService.getClasseMatiereById(note.getClasseMatiere().getClasseMatiereId());
        if (eleveOpt.isPresent() && classeMatiereOpt.isPresent()) {
            note.setEleve(eleveOpt.get());
            note.setClasseMatiere(classeMatiereOpt.get());
            note.setTrimestre(trimestreService.getActiveTrimestre());
            noteService.saveNote(note);
            redirectAttributes.addFlashAttribute("message", "Note ajoutée avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Eleve, Classe Matière ou Trimestre non trouvé");
            model.addAttribute("eleves", getEleveNomPrenomCombines());
            model.addAttribute("classeMatieres", getClasseMatiereCombines());
            return "notes/form";
        }
        return "redirect:/notes";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("note") Note note, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("eleves", eleveService.getAllEleves());
            model.addAttribute("classeMatieres", classeMatiereService.getAllClasseMatieres());
            return "notes/form";
        }
        note.setTrimestre(trimestreService.getActiveTrimestre());
        noteService.updateNote(id, note);
        redirectAttributes.addFlashAttribute("message", "Note mise à jour avec succès !");
        return "redirect:/notes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent()) {
            noteService.deleteNote(id);
            redirectAttributes.addFlashAttribute("message", "Note supprimée avec succès !");
            return "redirect:/notes";
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Note non trouvée !");
            return "erreur";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.severe("Exception survenue : " + e.getMessage());
        model.addAttribute("erreur", "Une erreur s'est produite : " + e.getMessage());
        return "erreur";
    }

    private List<String[]> getClasseMatiereCombines() {
        return classeMatiereService.getAllClasseMatieres().stream()
                .map(classeMatiere -> new String[] {
                        classeMatiere.getClasseMatiereId().toString(),
                        classeMatiere.getClasse().getNomClasse() + " - " + classeMatiere.getMatiere().getNomCourtMatiere() })
                .collect(Collectors.toList());
    }

    private List<String[]> getEleveNomPrenomCombines() {
        return eleveService.getAllEleves().stream()
                .map(eleve -> new String[] {
                        eleve.getEleveId().toString(),
                        eleve.getNom() + " " + eleve.getPrenom() })
                .collect(Collectors.toList());
    }

    private List<String[]> getTrimestreCombines() {
        return trimestreService.getAllTrimestres().stream()
                .map(trimestre -> new String[] {
                        trimestre.getTrimestreId().toString(),
                        trimestre.getNom() })
                .collect(Collectors.toList());
    }
}
