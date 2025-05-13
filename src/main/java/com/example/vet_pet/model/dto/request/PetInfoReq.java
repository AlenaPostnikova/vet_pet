package com.example.vet_pet.model.dto.request;

import com.example.vet_pet.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class PetInfoReq {

    @NotEmpty
    @Schema(description = "Кличка питомца")
    private String namePet;

    @Schema(description = "Дата рождения питомца")
    private LocalDate DateOfBirthPet;

    @NotEmpty
    @Schema(description = "Тип питомца")
    private String typePet;

    @Schema(description = "Порода питомца")
    private String breedPet;

    @Schema(description = "Окрас питомца")
    private String colorPet;

    @NotEmpty
    @Schema(description = "Пол питомца")
    private Gender genderPet;

}
