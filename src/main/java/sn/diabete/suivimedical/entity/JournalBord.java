package sn.diabete.suivimedical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "journal_bord")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalBord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId;

    private Long patientId; // Référence vers le patient concerné

    private String repas;               // Description du repas

    private String activitePhysique;    // Activité physique

    private String symptomes;           // Symptômes ressentis

    private String evenements;          // Événements de santé (stress, maladie…)

    private LocalDateTime dateSuivi;     // Date et heure d’enregistrement

    @PrePersist
    protected void onCreate() {
        this.dateSuivi = LocalDateTime.now();
    }
}
