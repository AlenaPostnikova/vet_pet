package com.example.vet_pet.model.dto.request;

import com.example.vet_pet.model.enums.Gender;
import com.example.vet_pet.model.enums.SpecializationDoctor;
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
public class DoctorInfoReq {

    @NotEmpty
    @Schema(description = "email")
    private String email;

    @NotEmpty
    @Schema(description = "Номер телефона")
    private String numPhone;

    @NotEmpty
    @Schema(description = "Пароль")
    private String password;

    @NotEmpty
    @Schema(description = "Имя")
    private String firstName;

    @NotEmpty
    @Schema(description = "Фамилия")
    private String lastName;

    @NotEmpty
    @Schema(description = "Отчество")
    private String middleName;

    @NotEmpty
    @Schema(description = "Дата рождения")
    private LocalDate dateOfBirth;

    @NotEmpty
    @Schema(description = "Специализация ветеринара")
    private SpecializationDoctor specDoctor;

    @NotEmpty
    @Schema(description = "Пол")
    private Gender gender;
}
