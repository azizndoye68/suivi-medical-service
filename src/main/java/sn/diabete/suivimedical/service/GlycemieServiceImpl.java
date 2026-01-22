package sn.diabete.suivimedical.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.diabete.suivimedical.analysis.GlycemieAnalysisService;
import sn.diabete.suivimedical.client.PatientClient;
import sn.diabete.suivimedical.dto.GlycemieRequest;
import sn.diabete.suivimedical.dto.GlycemieResponse;
import sn.diabete.suivimedical.entity.Glycemie;
import sn.diabete.suivimedical.exception.BadRequestException;
import sn.diabete.suivimedical.exception.ResourceNotFoundException;
import sn.diabete.suivimedical.mapper.GlycemieMapper;
import sn.diabete.suivimedical.messaging.GlycemieEventPublisher;
import sn.diabete.suivimedical.repository.GlycemieRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GlycemieServiceImpl implements GlycemieService {

    private final GlycemieRepository glycemieRepository;
    private final GlycemieMapper glycemieMapper;
    private final PatientClient patientClient;

    // 🆕 Nouveaux services injectés
    private final GlycemieAnalysisService analysisService;
    private final GlycemieEventPublisher eventPublisher;

    @Override
    public GlycemieResponse createSuivi(GlycemieRequest glycemieRequest) {

        // 🔎 Vérification du patient via Feign
        if (glycemieRequest.getPatientId() != null) {
            try {
                patientClient.getPatientById(glycemieRequest.getPatientId());
            } catch (Exception e) {
                throw new ResourceNotFoundException(
                        "Patient introuvable avec l'id : " + glycemieRequest.getPatientId()
                );
            }
        } else {
            throw new BadRequestException("Impossible de créer un suivi sans patientId.");
        }

        // Mapper et sauvegarder
        Glycemie glycemie = glycemieMapper.toSuivi(glycemieRequest);
        Glycemie saved = glycemieRepository.save(glycemie);

        // 🆕 ANALYSE AUTOMATIQUE DE LA GLYCÉMIE
        try {
            GlycemieAnalysisService.AnalysisResult analysisResult =
                    analysisService.analyzeGlycemie(saved);

            // Mettre à jour l'entité avec les résultats de l'analyse
            saved.setNiveauAlerte(analysisResult.getNiveauAlerte());
            saved.setTypeAlerte(analysisResult.getTypeAlerte());
            saved = glycemieRepository.save(saved);

            // 🆕 PUBLIER L'ÉVÉNEMENT SI GLYCÉMIE ANORMALE
            if (analysisResult.getTypeAlerte() != null) {
                eventPublisher.publishGlycemieEvent(analysisResult);
                saved.setEvenementEnvoye(true);
                glycemieRepository.save(saved);

                log.info("🚨 Alerte glycémie détectée pour patient {} - Type: {}",
                        saved.getPatientId(),
                        analysisResult.getTypeAlerte());
            }

            log.info("✅ Glycémie créée et analysée pour patient {} - Niveau: {}",
                    saved.getPatientId(),
                    analysisResult.getNiveauAlerte());

        } catch (Exception e) {
            log.error("❌ Erreur lors de l'analyse/publication pour glycémie {}: {}",
                    saved.getId(), e.getMessage());
            // On continue même si l'analyse échoue, la mesure est sauvegardée
        }

        return glycemieMapper.toSuiviResponse(saved);
    }

    @Override
    public List<GlycemieResponse> getAllSuivis() {
        List<Glycemie> glycemies = glycemieRepository.findAll();
        return glycemieMapper.toSuiviResponseList(glycemies);
    }

    @Override
    public GlycemieResponse getSuiviById(Long id) {
        Glycemie glycemie = glycemieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Suivi non trouvé avec l'id : " + id
                ));
        return glycemieMapper.toSuiviResponse(glycemie);
    }

    @Override
    public GlycemieResponse updateSuivi(Long id, GlycemieRequest glycemieRequest) {
        Glycemie existingGlycemie = glycemieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Suivi non trouvé avec l'id : " + id
                ));

        existingGlycemie.setGlycemie(glycemieRequest.getGlycemie());
        existingGlycemie.setMoment(glycemieRequest.getMoment());
        existingGlycemie.setRepas(glycemieRequest.getRepas());

        // 🆕 Réanalyser après modification
        try {
            GlycemieAnalysisService.AnalysisResult analysisResult =
                    analysisService.analyzeGlycemie(existingGlycemie);

            existingGlycemie.setNiveauAlerte(analysisResult.getNiveauAlerte());
            existingGlycemie.setTypeAlerte(analysisResult.getTypeAlerte());

            // Republier si nécessaire
            if (analysisResult.getTypeAlerte() != null && !Boolean.TRUE.equals(existingGlycemie.getEvenementEnvoye())) {
                eventPublisher.publishGlycemieEvent(analysisResult);
                existingGlycemie.setEvenementEnvoye(true);
            }

        } catch (Exception e) {
            log.error("Erreur lors de la réanalyse de glycémie {}: {}", id, e.getMessage());
        }

        Glycemie updated = glycemieRepository.save(existingGlycemie);
        return glycemieMapper.toSuiviResponse(updated);
    }

    @Override
    public void deleteSuivi(Long id) {
        Glycemie glycemie = glycemieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Suivi non trouvé avec l'id : " + id
                ));
        glycemieRepository.delete(glycemie);
    }

    @Override
    public GlycemieResponse getLastSuiviByPatientId(Long patientId) {
        return glycemieRepository.findTopByPatientIdOrderByDateSuiviDesc(patientId)
                .map(glycemieMapper::toSuiviResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucune mesure trouvée pour le patient : " + patientId
                ));
    }

    @Override
    public List<GlycemieResponse> getRecentSuivisByPatientId(Long patientId) {
        List<Glycemie> glycemies =
                glycemieRepository.findTop7ByPatientIdOrderByDateSuiviDesc(patientId);
        return glycemieMapper.toSuiviResponseList(glycemies);
    }
}