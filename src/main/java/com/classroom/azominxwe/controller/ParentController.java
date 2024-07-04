package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @GetMapping("/moyenne")
    public ResponseEntity<String> getMoyenne(@RequestParam String matricule) {
        String result = parentService.getMoyenneByMatricule(matricule);
        if (result.equals("Matricule invalide") || result.equals("Aucun trimestre actif trouvé pour l'année académique en cours") || result.equals("Aucun trimestre précédent trouvé")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/matiere")
    public ResponseEntity<String> getMoyenneMatiere(@RequestParam String matricule, @RequestParam Long matiereId) {
        String result = parentService.getMoyenneByMatriculeAndMatiere(matricule, matiereId);
        if (result.equals("Matricule invalide") || result.equals("Aucun trimestre actif trouvé pour l'année académique en cours") || result.equals("Matière invalide") || result.equals("Aucune moyenne trouvée pour cette matière")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }
}
