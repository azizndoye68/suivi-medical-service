package sn.diabete.suivimedical.service;

import sn.diabete.suivimedical.dto.SuiviRequest;
import sn.diabete.suivimedical.dto.SuiviResponse;

import java.util.List;

public interface SuiviService {

    SuiviResponse createSuivi(SuiviRequest suiviRequest);

    List<SuiviResponse> getAllSuivis();

    SuiviResponse getSuiviById(Long id);

    SuiviResponse updateSuivi(Long id, SuiviRequest suiviRequest);

    void deleteSuivi(Long id);

    SuiviResponse getLastSuiviByPatientId(Long patientId);

    List<SuiviResponse> getRecentSuivisByPatientId(Long patientId);

}
