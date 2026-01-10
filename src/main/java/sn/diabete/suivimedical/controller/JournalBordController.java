package sn.diabete.suivimedical.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.diabete.suivimedical.dto.JournalBordRequest;
import sn.diabete.suivimedical.dto.JournalBordResponse;
import sn.diabete.suivimedical.service.JournalBordService;

import java.util.List;

@RestController
@RequestMapping("/api/journal-bord")
@RequiredArgsConstructor
public class JournalBordController {

    private final JournalBordService service;

    // ✅ Créer une entrée de journal
    @PostMapping
    public ResponseEntity<JournalBordResponse> create(
            @RequestBody JournalBordRequest request
    ) {
        return new ResponseEntity<>(
                service.create(request),
                HttpStatus.CREATED
        );
    }

    // ✅ Récupérer une entrée par ID
    @GetMapping("/{id}")
    public ResponseEntity<JournalBordResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ Récupérer le journal d’un patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<JournalBordResponse>> getByPatient(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(service.getByPatientId(patientId));
    }

    // ✅ Modifier une entrée
    @PutMapping("/{id}")
    public ResponseEntity<JournalBordResponse> update(
            @PathVariable Long id,
            @RequestBody JournalBordRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // ✅ Supprimer une entrée
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
