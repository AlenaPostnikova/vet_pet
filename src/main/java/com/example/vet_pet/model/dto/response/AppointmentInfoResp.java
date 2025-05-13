package com.example.vet_pet.model.dto.response;

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
public class AppointmentInfoResp {
    private Long id;
    private TimetableInfoResp timetableInfoResp;
    private UserInfoResp userInfoResp;
    private PetInfoResp petInfoResp;

}
