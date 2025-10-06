package sn.diabete.suivimedical.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.diabete.suivimedical.dto.SuiviRequest;
import sn.diabete.suivimedical.dto.SuiviResponse;
import sn.diabete.suivimedical.entity.Suivi;
import sn.diabete.suivimedical.exception.ResourceNotFoundException;
import sn.diabete.suivimedical.mapper.SuiviMapper;
import sn.diabete.suivimedical.repository.SuiviRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuiviServiceImpl implements SuiviService {

    private final SuiviRepository suiviRepository;
    private final SuiviMapper suiviMapper;

    @Override
    public SuiviResponse createSuivi(SuiviRequest suiviRequest) {
        Suivi suivi = suiviMapper.toSuivi(suiviRequest);
        Suivi savedSuivi = suiviRepository.save(suivi);
        return suiviMapper.toSuiviResponse(savedSuivi);
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

        existingSuivi.setPatientId(suiviRequest.getPatientId());
        existingSuivi.setGlycemie(suiviRequest.getGlycemie());
        //existingSuivi.setPoids(suiviRequest.getPoids());
        existingSuivi.setMoment(suiviRequest.getMoment());
        existingSuivi.setRepas(suiviRequest.getRepas());
        existingSuivi.setInsuline(suiviRequest.getInsuline());
        existingSuivi.setActivite(suiviRequest.getActivite());
        existingSuivi.setSymptome(suiviRequest.getSymptome());
        //existingSuivi.setEvenement(suiviRequest.getEvenement());

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
