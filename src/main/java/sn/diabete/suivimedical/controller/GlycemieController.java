package sn.diabete.suivimedical.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.diabete.suivimedical.dto.GlycemieRequest;
import sn.diabete.suivimedical.dto.GlycemieResponse;
import sn.diabete.suivimedical.service.GlycemieService;

import java.util.List;

@RestController
@RequestMapping("/api/suivis")
@RequiredArgsConstructor
public class GlycemieController {

    private final GlycemieService glycemieService;

    // 👉 Créer un suivi
    @PostMapping
    public ResponseEntity<GlycemieResponse> createSuivi(@RequestBody GlycemieRequest glycemieRequest) {
        GlycemieResponse glycemieResponse = glycemieService.createSuivi(glycemieRequest);
        return ResponseEntity.ok(glycemieResponse);
    }

    // 👉 Récupérer tous les suivis
    @GetMapping
    public ResponseEntity<List<GlycemieResponse>> getAllSuivis() {
        List<GlycemieResponse> suivis = glycemieService.getAllSuivis();
        return ResponseEntity.ok(suivis);
    }

    // 👉 Récupérer un suivi par ID
    @GetMapping("/{id}")
    public ResponseEntity<GlycemieResponse> getSuiviById(@PathVariable Long id) {
        GlycemieResponse glycemieResponse = glycemieService.getSuiviById(id);
        return ResponseEntity.ok(glycemieResponse);
    }

    // 👉 Mettre à jour un suivi
    @PutMapping("/{id}")
    public ResponseEntity<GlycemieResponse> updateSuivi(@PathVariable Long id, @RequestBody GlycemieRequest glycemieRequest) {
        GlycemieResponse updatedSuivi = glycemieService.updateSuivi(id, glycemieRequest);
        return ResponseEntity.ok(updatedSuivi);
    }

    // 👉 Supprimer un suivi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuivi(@PathVariable Long id) {
        glycemieService.deleteSuivi(id);
        return ResponseEntity.noContent().build();
    }

    // 👉 Dernière mesure de glycémie
    @GetMapping("/last")
    public ResponseEntity<GlycemieResponse> getLastGlycemie(@RequestParam Long patientId) {
        GlycemieResponse dernierSuivi = glycemieService.getLastSuiviByPatientId(patientId);
        return ResponseEntity.ok(dernierSuivi);
    }

    // 👉 Les 7 dernières mesures de glycémie
    @GetMapping("/recentes")
    public ResponseEntity<List<GlycemieResponse>> getRecentSuivis(@RequestParam Long patientId) {
        List<GlycemieResponse> suivis = glycemieService.getRecentSuivisByPatientId(patientId);
        return ResponseEntity.ok(suivis);
    }

}
