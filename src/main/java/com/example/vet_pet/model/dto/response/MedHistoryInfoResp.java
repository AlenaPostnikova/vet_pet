package com.example.vet_pet.model.dto.response;

import com.example.vet_pet.model.dto.request.MedHistoryInfoReq;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedHistoryInfoResp extends MedHistoryInfoReq {
    private Long id;
    private AppointmentInfoResp appointmentInfoResp;
    private PetInfoResp petInfoResp;

}
