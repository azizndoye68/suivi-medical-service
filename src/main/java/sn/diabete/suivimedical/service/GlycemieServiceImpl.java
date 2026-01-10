package sn.diabete.suivimedical.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.diabete.suivimedical.client.PatientClient;
import sn.diabete.suivimedical.dto.GlycemieRequest;
import sn.diabete.suivimedical.dto.GlycemieResponse;
import sn.diabete.suivimedical.dto.PatientDTO;
import sn.diabete.suivimedical.entity.Glycemie;
import sn.diabete.suivimedical.exception.BadRequestException;
import sn.diabete.suivimedical.exception.ResourceNotFoundException;
import sn.diabete.suivimedical.mapper.GlycemieMapper;
import sn.diabete.suivimedical.repository.GlycemieRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GlycemieServiceImpl implements GlycemieService {

    private final GlycemieRepository glycemieRepository;
    private final GlycemieMapper glycemieMapper;
    private final PatientClient patientClient;

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

        Glycemie glycemie = glycemieMapper.toSuivi(glycemieRequest);
        Glycemie saved = glycemieRepository.save(glycemie);
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
