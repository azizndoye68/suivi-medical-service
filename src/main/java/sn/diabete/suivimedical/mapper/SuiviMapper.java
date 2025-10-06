package sn.diabete.suivimedical.mapper;


import org.mapstruct.Mapper;
import sn.diabete.suivimedical.dto.SuiviRequest;
import sn.diabete.suivimedical.dto.SuiviResponse;
import sn.diabete.suivimedical.entity.Suivi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SuiviMapper {


    Suivi toSuivi(SuiviRequest suiviRequest);

    SuiviResponse toSuiviResponse(Suivi suivi);

    List<SuiviResponse> toSuiviResponseList(List<Suivi> suivis);
}
