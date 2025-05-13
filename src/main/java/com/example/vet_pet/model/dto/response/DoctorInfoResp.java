package com.example.vet_pet.model.dto.response;

import com.example.vet_pet.model.dto.request.DoctorInfoReq;
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
public class DoctorInfoResp extends DoctorInfoReq {
    private Long id;
}
