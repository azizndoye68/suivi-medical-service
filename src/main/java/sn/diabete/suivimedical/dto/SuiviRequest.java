package sn.diabete.suivimedical.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SuiviRequest {

    private Long patientId; // Référence vers le patient concerné

    private Double glycemie;           // Taux de glycémie (g/l)

    private String moment;             // Moment de la prise (avant_repas, apres_repas, etc.)

    private String repas;              // Type de repas (petit_dejeuner, dejeuner, etc.)

    private String insuline;           // Type d’insuline (rapide, lente, etc.)

    private String activite;           // Activité physique (légère, modérée, etc.)

    private String symptome;           // Symptômes (hypoglycémie, vertige, etc.)

   // private String evenement;          // Événement de santé (stress, maladie, etc.)

   // private Double poids;              // Poids (Kg)

}

