package sn.diabete.suivimedical.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import sn.diabete.suivimedical.dto.SuiviRequest;
import sn.diabete.suivimedical.dto.SuiviResponse;
import sn.diabete.suivimedical.entity.Suivi;
import sn.diabete.suivimedical.exception.BadRequestException;
import sn.diabete.suivimedical.exception.ResourceNotFoundException;
import sn.diabete.suivimedical.mapper.SuiviMapper;
import sn.diabete.suivimedical.repository.SuiviRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SuiviServiceImpl implements SuiviService {

    private final SuiviRepository suiviRepository;
    private final SuiviMapper suiviMapper;
    private final RestTemplate restTemplate;

    // ✅ Adresse du microservice Patient (à adapter selon ton environnement)
    private static final String PATIENT_SERVICE_URL = "http://localhost:8082/api/patients/byUtilisateur/";

    @Override
    public SuiviResponse createSuivi(SuiviRequest suiviRequest) {

        // 🔎 Vérification si on reçoit utilisateurId au lieu de patientId
        if (suiviRequest.getPatientId() == null && suiviRequest.getUtilisateurId() != null) {

            // ✅ Appel inter-microservice vers PatientService
            String url = PATIENT_SERVICE_URL + suiviRequest.getUtilisateurId();

            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                Map<String, Object> patientData = response.getBody();

                if (patientData != null && patientData.get("id") != null) {
                    Long patientId = ((Number) patientData.get("id")).longValue();
                    suiviRequest.setPatientId(patientId);
                } else {
                    throw new ResourceNotFoundException("Aucun patient trouvé pour utilisateurId : " + suiviRequest.getUtilisateurId());
                }

            } catch (Exception e) {
                throw new ResourceNotFoundException("Erreur lors de la récupération du patient : " + e.getMessage());
            }
        }

        if (suiviRequest.getPatientId() == null) {
            throw new BadRequestException("Impossible de créer un suivi sans patientId valide.");
        }

        Suivi suivi = suiviMapper.toSuivi(suiviRequest);
        Suivi saved = suiviRepository.save(suivi);
        return suiviMapper.toSuiviResponse(saved);
    }

    @Override
    public List<SuiviResponse> getAllSuivis() {
        List<Suivi> suivis = suiviRepository.findAll();
        return suiviMapper.toSuiviResponseList(suivis);
    }

    @Override
    public SuiviResponse getSuiviById(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suivi non trouvé avec l'id : " + id));
        return suiviMapper.toSuiviResponse(suivi);
    }

    @Override
    public SuiviResponse updateSuivi(Long id, SuiviRequest suiviRequest) {
        Suivi existingSuivi = suiviRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suivi non trouvé avec l'id : " + id));

        existingSuivi.setGlycemie(suiviRequest.getGlycemie());
        existingSuivi.setMoment(suiviRequest.getMoment());
        existingSuivi.setRepas(suiviRequest.getRepas());
        existingSuivi.setInsuline(suiviRequest.getInsuline());
        existingSuivi.setActivite(suiviRequest.getActivite());
        existingSuivi.setSymptome(suiviRequest.getSymptome());

        Suivi updatedSuivi = suiviRepository.save(existingSuivi);
        return suiviMapper.toSuiviResponse(updatedSuivi);
    }

    @Override
    public void deleteSuivi(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suivi non trouvé avec l'id : " + id));
        suiviRepository.delete(suivi);
    }

    @Override
    public SuiviResponse getLastSuiviByPatientId(Long patientId) {
        return suiviRepository.findTopByPatientIdOrderByDateSuiviDesc(patientId)
                .map(suiviMapper::toSuiviResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Aucune mesure trouvée pour le patient : " + patientId));
    }

    @Override
    public List<SuiviResponse> getRecentSuivisByPatientId(Long patientId) {
        List<Suivi> suivis = suiviRepository.findTop7ByPatientIdOrderByDateSuiviDesc(patientId);
        return suiviMapper.toSuiviResponseList(suivis);
    }
}
