package sn.diabete.suivimedical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlycemieResponse {

    private Long id;

    private Long utilisateurId;

    private Long patientId; // Référence vers le patient concerné

    private Double glycemie;           // Taux de glycémie (g/l)

    private String moment;             // Moment de la prise (avant_repas, apres_repas, etc.)

    private String repas;              // Type de repas (petit_dejeuner, dejeuner, etc.)

    private LocalDateTime dateSuivi;        // Date et heure d’enregistrement
}
