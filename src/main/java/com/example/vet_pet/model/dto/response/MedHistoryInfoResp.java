package com.example.vet_pet.model.dto.response;

import com.example.vet_pet.model.dto.request.MedHistoryInfoReq;
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
public class MedHistoryInfoResp extends MedHistoryInfoReq {
    private Long id;
    private AppointmentInfoResp appointmentInfoResp;
    private PetInfoResp petInfoResp;

}
