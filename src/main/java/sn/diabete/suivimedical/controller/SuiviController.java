package sn.diabete.suivimedical.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.diabete.suivimedical.dto.SuiviRequest;
import sn.diabete.suivimedical.dto.SuiviResponse;
import sn.diabete.suivimedical.service.SuiviService;

import java.util.List;

@RestController
@RequestMapping("/api/suivis")
@RequiredArgsConstructor
public class SuiviController {

    private final SuiviService suiviService;

    // 👉 Créer un suivi
    @PostMapping
    public ResponseEntity<SuiviResponse> createSuivi(@RequestBody SuiviRequest suiviRequest) {
        SuiviResponse suiviResponse = suiviService.createSuivi(suiviRequest);
        return ResponseEntity.ok(suiviResponse);
    }

    // 👉 Récupérer tous les suivis
    @GetMapping
    public ResponseEntity<List<SuiviResponse>> getAllSuivis() {
        List<SuiviResponse> suivis = suiviService.getAllSuivis();
        return ResponseEntity.ok(suivis);
    }

    // 👉 Récupérer un suivi par ID
    @GetMapping("/{id}")
    public ResponseEntity<SuiviResponse> getSuiviById(@PathVariable Long id) {
        SuiviResponse suiviResponse = suiviService.getSuiviById(id);
        return ResponseEntity.ok(suiviResponse);
    }

    // 👉 Mettre à jour un suivi
    @PutMapping("/{id}")
    public ResponseEntity<SuiviResponse> updateSuivi(@PathVariable Long id, @RequestBody SuiviRequest suiviRequest) {
        SuiviResponse updatedSuivi = suiviService.updateSuivi(id, suiviRequest);
        return ResponseEntity.ok(updatedSuivi);
    }

    // 👉 Supprimer un suivi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuivi(@PathVariable Long id) {
        suiviService.deleteSuivi(id);
        return ResponseEntity.noContent().build();
    }

    // 👉 Dernière mesure de glycémie
    @GetMapping("/last")
    public ResponseEntity<SuiviResponse> getLastGlycemie(@RequestParam Long patientId) {
        SuiviResponse dernierSuivi = suiviService.getLastSuiviByPatientId(patientId);
        return ResponseEntity.ok(dernierSuivi);
    }

    // 👉 Les 7 dernières mesures de glycémie
    @GetMapping("/recentes")
    public ResponseEntity<List<SuiviResponse>> getRecentSuivis(@RequestParam Long patientId) {
        List<SuiviResponse> suivis = suiviService.getRecentSuivisByPatientId(patientId);
        return ResponseEntity.ok(suivis);
    }

}
