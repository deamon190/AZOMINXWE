package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.AnneeAcademique;
import com.classroom.azominxwe.model.CustomUserDetails;
import com.classroom.azominxwe.model.Trimestre;
import com.classroom.azominxwe.service.AnneeAcademiqueService;
import com.classroom.azominxwe.service.StatistiqueService;
import com.classroom.azominxwe.service.TrimestreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private StatistiqueService statistiqueService;
    @Autowired
    private TrimestreService trimestreService;

    @Autowired
    private AnneeAcademiqueService anneeAcademiqueService;

    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("fullName", userDetails.getFullName());
            model.addAttribute("imageUrl", userDetails.getImageUrl());
        }
        Trimestre trimestreActif = trimestreService.getActiveTrimestresForActiveAnneeAcademique();
        AnneeAcademique anneeAcademiqueActif = anneeAcademiqueService.getActiveAnneeAcademique();

        if(trimestreActif==null || anneeAcademiqueActif==null )
        {
            model.addAttribute("trimestreActif", null);
        }
        else {
            model.addAttribute("trimestreActif", trimestreActif.getNom()+ " (" + anneeAcademiqueActif.getAnnee() + ")");

        }
        model.addAttribute("nombreEleves", statistiqueService.getNombreEleves());
        model.addAttribute("nombreEnseignants", statistiqueService.getNombreEnseignants());
        model.addAttribute("nombreClasses", statistiqueService.getNombreClasses());
    }
}
