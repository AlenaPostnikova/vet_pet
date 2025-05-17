package com.example.vet_pet.model.dto.response;

import com.example.vet_pet.model.dto.request.TimetableInfoReq;
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
public class TimetableInfoResp extends TimetableInfoReq {
    private Long id;
    private DoctorInfoResp doctorInfoResp;
}
