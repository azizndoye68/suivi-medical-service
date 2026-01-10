package sn.diabete.suivimedical.dto;

import lombok.Data;

@Data
public class DonneePhysiqueRequest {

    private Long utilisateurId;
    private Long patientId;

    private Double poids;
    private String tension;
}
