package sn.diabete.suivimedical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suivis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suivi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId;

    private Long patientId; // Référence vers le patient concerné

    private Double glycemie;           // Taux de glycémie (g/l)

    private String moment;             // Moment de la prise (avant_repas, apres_repas, etc.)

    private String repas;              // Type de repas (petit_dejeuner, dejeuner, etc.)

    //private String insuline;           // Type d’insuline (rapide, lente, etc.)

    //private String activite;           // Activité physique (légère, modérée, etc.)

    //private String symptome;           // Symptômes (hypoglycémie, vertige, etc.)

    //private String evenement;          // Événement de santé (stress, maladie, etc.)

    //private Double poids;              // Poids (Kg)

    private LocalDateTime dateSuivi;        // Date et heure d’enregistrement

    @PrePersist
    protected void onCreate() {
        this.dateSuivi = LocalDateTime.now();
    }
}
