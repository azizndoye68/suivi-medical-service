package sn.diabete.suivimedical.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sn.diabete.suivimedical.entity.Glycemie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GlycemieRepository extends JpaRepository<Glycemie, Long> {

    //Récupérer tous les suivis d'un patient donné par son ID
    List<Glycemie> findByPatientId(Long patientId);

    //Optionnel : récupérer les suivis par date (si besoin)
    List<Glycemie> findByDateSuivi(LocalDateTime dateSuivi);

    Optional<Glycemie> findTopByPatientIdOrderByDateSuiviDesc(Long patientId);
    List<Glycemie> findTop7ByPatientIdOrderByDateSuiviDesc(Long patientId);

}
