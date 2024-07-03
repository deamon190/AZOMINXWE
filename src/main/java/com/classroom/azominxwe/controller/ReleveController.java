package com.classroom.azominxwe.controller;

import com.classroom.azominxwe.model.*;
import com.classroom.azominxwe.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ReleveController {

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MoyenneTrimestreRepository moyenneTrimestreRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private ClasseMatiereRepository classeMatiereRepository;

    @Autowired
    private MoyenneMatiereRepository moyenneMatiereRepository;

    @GetMapping("/eleves/{id}/releve")
    public void downloadReleve(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request, Model model) throws IOException {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid eleve Id:" + id));
        Trimestre trimestreActif = trimestreRepository.findByActifTrue();
        MoyenneTrimestre moyenneTrimestre = moyenneTrimestreRepository.findFirstByEleveAndTrimestre(eleve, trimestreActif);

        if (moyenneTrimestre == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Aucune moyenne trouvée pour le trimestre actif");
            return;
        }

        List<ClasseMatiere> classeMatieres = classeMatiereRepository.findByClasse_ClasseId(moyenneTrimestre.getClasse().getClasseId());
        List<MoyenneMatiere> moyennesMatieres = moyenneMatiereRepository.findByEleveAndTrimestre(eleve, trimestreActif);
        Map<Long, MoyenneMatiere> moyennesMatieresMap = moyennesMatieres.stream()
                .collect(Collectors.toMap(mm -> mm.getClasseMatiere().getClasseMatiereId(), mm -> mm, (mm1, mm2) -> mm1)); // Handling duplicate keys

        model.addAttribute("eleve", eleve);
        model.addAttribute("classeMatieres", classeMatieres);
        model.addAttribute("trimestre", trimestreActif);
        model.addAttribute("moyenne", moyenneTrimestre.getMoyenne());
        model.addAttribute("moyennesMatieresMap", moyennesMatieresMap);

        // Construire l'URL absolue de l'image
        String imageUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/login_logo.png";

        // Convert HTML to PDF
        String htmlContent = renderHtml(model, imageUrl);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(baos);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=releve_" + eleve.getMatricule() + "_" + eleve.getNom() + "_" + trimestreActif.getNom() + "_" + trimestreActif.getAnneeAcademique().getAnnee() + ".pdf");
        response.setContentLength(baos.size());
        response.getOutputStream().write(baos.toByteArray());
    }



    private String renderHtml(Model model, String imageUrl) {
        Eleve eleve = (Eleve) model.getAttribute("eleve");
        List<ClasseMatiere> classeMatieres = (List<ClasseMatiere>) model.getAttribute("classeMatieres");
        Trimestre trimestre = (Trimestre) model.getAttribute("trimestre");
        Double moyenne = (Double) model.getAttribute("moyenne");
        Map<Long, MoyenneMatiere> moyennesMatieresMap = (Map<Long, MoyenneMatiere>) model.getAttribute("moyennesMatieresMap");

        StringBuilder html = new StringBuilder();
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style>")
                .append("body { font-family: Arial, sans-serif; }")
                .append("table { width: 100%; border-collapse: collapse; }")
                .append("table, th, td { border: 1px solid black; padding: 8px; text-align: left; }")
                .append("th { background-color: #EFF3F6; }")
                .append(".recap_moy { background-color: #c5eded; }")
                .append(".note-missing { background-color: #FFEEEE; }") // Adding style for missing notes
                .append("</style></head><body>");

        html.append("<header>")
                .append("<div style='text-align: center;'>")
                .append("<img src='").append(imageUrl).append("' alt='Logo' style='width: 200px; height: auto;' />")
                .append("<h2>Ecole secondaire <span style='bold;'> Les anges de l'éternel </span> </h2>")
                .append("<p style='font-size:13px; color:#779ecb;'>Téléphone: +229 665 475 25 | Email: groupe4@eneam_uac.bj | Site web: www.groupeigformaster.bj</p>")
                .append("</div>")
                .append("</header>")
                .append("<hr/>");

        html.append("<h3>Année Académique: ").append(trimestre.getAnneeAcademique().getAnnee()).append("</h3>");
        String nomTrimestre = trimestre.getNom();
        Classe classe = classeMatieres.isEmpty() ? null : classeMatieres.get(0).getClasse();
        html.append("<p> <h2>Relevé de Notes du ").append(nomTrimestre).append("</h2></p>")
                .append("<p><b>Matricule:</b> ").append(eleve.getMatricule()).append("</p>")
                .append("<p><b>Nom:</b> ").append(eleve.getNom()).append(" ").append(eleve.getPrenom()).append("</p>")
                .append("<p><b>Classe:</b> ").append(classe != null ? classe.getNomClasse() : "").append("</p>");

        html.append("<table><tr>")
                .append("<th>N°</th><th>Matière</th><th>Moyenne</th><th>Coeff</th><th>Moy Coefficientée</th>")
                .append("</tr>");
        Integer i = 1;
        for (ClasseMatiere cm : classeMatieres) {
            MoyenneMatiere moyenneMatiere = moyennesMatieresMap.get(cm.getClasseMatiereId());
            double moyenneMatiereValue = moyenneMatiere != null ? moyenneMatiere.getMoyenne() : 0.0;
            Integer coef = cm.getCoefficient();
            double noteCoef = moyenneMatiereValue * coef;

            Boolean noteExists = noteRepository.existsByClasseMatiere_ClasseMatiereIdAndTrimestre_TrimestreIdAndEleve_EleveId
                    (cm.getClasseMatiereId(), ((Trimestre) model.getAttribute("trimestre")).getTrimestreId(), eleve.getEleveId());

            html.append("<tr>")
                    .append("<td>").append(i).append("</td>")
                    .append("<td>").append(cm.getMatiere().getNomMatiere()).append("</td>")
                    .append("<td class='").append(!noteExists ? "note-missing" : "").append("'>").append(moyenneMatiereValue).append("</td>")
                    .append("<td>").append(coef).append("</td>")
                    .append("<td>").append(noteCoef).append("</td>")
                    .append("</tr>");
            i++;
        }

        html.append("</table>")
                .append("<p>Moyenne Trimestrielle: <b>").append(moyenne).append("</b></p>");

        if (Objects.equals(nomTrimestre, "Trimestre 3")) {
            Double moyenneAnnuelle = calculerMoyenneAnnuelle(eleve, trimestre.getAnneeAcademique());
            List<MoyenneTrimestre> moyennesTrimestres = moyenneTrimestreRepository.findByEleveAndTrimestre_AnneeAcademique(eleve, trimestre.getAnneeAcademique());

            // Initialiser les moyennes des trimestres à zéro
            Map<String, Double> moyennesTrimestresMap = new HashMap<>();
            moyennesTrimestresMap.put("Trimestre 1", 0.0);
            moyennesTrimestresMap.put("Trimestre 2", 0.0);
            moyennesTrimestresMap.put("Trimestre 3", 0.0);

            // Remplir les moyennes existantes
            for (MoyenneTrimestre mt : moyennesTrimestres) {
                moyennesTrimestresMap.put(mt.getTrimestre().getNom(), mt.getMoyenne());
            }

            html
                    .append("<table><tr><th class='recap_moy'>Trimestre 1</th><th class='recap_moy'>Trimestre 2</th>" +
                            "<th class='recap_moy'>Trimestre 3</th></tr>")  .append("<tr>")
                    .append("<td>").append(moyennesTrimestresMap.get("Trimestre 1")).append("</td>")
                    .append("<td>").append(moyennesTrimestresMap.get("Trimestre 2")).append("</td>")
                    .append("<td>").append(moyennesTrimestresMap.get("Trimestre 3")).append("</td>")
                    .append("</tr>")
                    .append("<tr><td colspan=\"3\"><b>Moyenne Annuelle: ").append(moyenneAnnuelle).append("</b></td></tr>")
                    .append("</table>");
        }

        html.append("<footer>")
                .append("<div style='text-align: center;'>")
                .append("<div style='text-align: right; margin-top: 80px;'>")
                .append("<p style=\"margin-bottom:0px;\">FADOHOUN H. P. Anicet</p>")
                .append("<p style=\"margin-top:0px;\">__________________________</p>")
                .append("</div>")
                .append("<hr/>")
                .append("<p>Relevé généré le: ").append(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("</p>")
                .append("</div>")
                .append("</footer>");

        html.append("</body></html>");

        return html.toString();
    }



    private Double calculerMoyenneAnnuelle(Eleve eleve, AnneeAcademique anneeAcademique) {
        List<MoyenneTrimestre> moyennesTrimestres = moyenneTrimestreRepository.findByEleveAndTrimestre_AnneeAcademique(eleve, anneeAcademique);
        return moyennesTrimestres.stream().mapToDouble(MoyenneTrimestre::getMoyenne).average().orElse(0.0);
    }
}
