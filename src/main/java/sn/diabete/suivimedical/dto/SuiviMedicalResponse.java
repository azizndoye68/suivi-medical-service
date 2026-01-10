package sn.diabete.suivimedical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuiviMedicalResponse {

    private Long id;

    private Long patientId;

    private LocalDateTime dateSuivi;

    private List<GlycemieResponse> glycemies;

    private List<DonneePhysiqueResponse> donneesPhysiques;

    private JournalBordResponse journalBord;
}
