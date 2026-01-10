package sn.diabete.suivimedical.dto;

import lombok.Data;

import java.util.List;

@Data
public class SuiviMedicalRequest {

    /**
     * Référence du patient (microservice Patient)
     */
    private Long patientId;

    /**
     * Liste des glycémies enregistrées
     */
    private List<GlycemieRequest> glycemies;

    /**
     * Données physiques associées
     */
    private List<DonneePhysiqueRequest> donneesPhysiques;

    /**
     * Journal de bord (optionnel)
     */
    private JournalBordRequest journalBord;
}
