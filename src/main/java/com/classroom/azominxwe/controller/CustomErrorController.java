package com.classroom.azominxwe.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        // Vous pouvez ajouter une logique ici pour gérer les erreurs
        return "erreur"; // Retourne une vue appelée "error"
    }
}
