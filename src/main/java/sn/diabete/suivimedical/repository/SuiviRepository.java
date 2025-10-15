package sn.diabete.suivimedical.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sn.diabete.suivimedical.entity.Suivi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SuiviRepository extends JpaRepository<Suivi, Long> {

    //Récupérer tous les suivis d'un patient donné par son ID
    List<Suivi> findByPatientId(Long patientId);

    //Optionnel : récupérer les suivis par date (si besoin)
    List<Suivi> findByDateSuivi(LocalDateTime dateSuivi);

    Optional<Suivi> findTopByPatientIdOrderByDateSuiviDesc(Long patientId);
    List<Suivi> findTop7ByPatientIdOrderByDateSuiviDesc(Long patientId);

}
