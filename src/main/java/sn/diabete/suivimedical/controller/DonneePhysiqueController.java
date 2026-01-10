package sn.diabete.suivimedical.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.diabete.suivimedical.dto.DonneePhysiqueRequest;
import sn.diabete.suivimedical.dto.DonneePhysiqueResponse;
import sn.diabete.suivimedical.service.DonneePhysiqueService;

import java.util.List;

@RestController
@RequestMapping("/api/donnees-physiques")
@RequiredArgsConstructor
public class DonneePhysiqueController {

    private final DonneePhysiqueService service;

    // ✅ Créer une donnée physique
    @PostMapping
    public ResponseEntity<DonneePhysiqueResponse> create(
            @RequestBody DonneePhysiqueRequest request
    ) {
        return new ResponseEntity<>(
                service.create(request),
                HttpStatus.CREATED
        );
    }

    // ✅ Récupérer une donnée par ID
    @GetMapping("/{id}")
    public ResponseEntity<DonneePhysiqueResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ Récupérer toutes les données d’un patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DonneePhysiqueResponse>> getByPatient(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(service.getByPatientId(patientId));
    }

    // ✅ Modifier une donnée
    @PutMapping("/{id}")
    public ResponseEntity<DonneePhysiqueResponse> update(
            @PathVariable Long id,
            @RequestBody DonneePhysiqueRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // ✅ Supprimer une donnée
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
