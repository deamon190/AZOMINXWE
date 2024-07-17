package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.dto.EleveDTO;
import com.classroom.azominxwe.model.Eleve;
import com.classroom.azominxwe.service.EleveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/eleves")
public class EleveRestController {

    private static final Logger logger = Logger.getLogger(EleveRestController.class.getName());

    @Autowired
    private EleveService eleveService;

    @GetMapping
    public ResponseEntity<List<EleveDTO>> getAllEleves() {
        List<EleveDTO> eleves = eleveService.getAllEleveDTOs();
        return ResponseEntity.ok(eleves);
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<EleveDTO> getEleveByMatricule(@PathVariable String matricule) {
        Optional<EleveDTO> eleve = eleveService.getEleveDTOByMatricule(matricule);
        return eleve.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEleve(@Valid @RequestBody EleveDTO eleveDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        EleveDTO savedEleve = eleveService.saveEleveDTO(eleveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEleve);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEleve(@PathVariable Long id, @Valid @RequestBody EleveDTO eleveDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        Optional<Eleve> existingEleve = eleveService.getEleveById(id);
        if (existingEleve.isPresent()) {
            eleveService.updateDTO(id, eleveDTO);
            eleveDTO.setEleveId(id);
            return ResponseEntity.ok(eleveDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEleve(@PathVariable Long id) {
        Optional<Eleve> eleve = eleveService.getEleveById(id);
        if (eleve.isPresent()) {
            eleveService.deleteEleve(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, String> formatValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
