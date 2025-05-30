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
public class UserInfoReq {

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

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Дата рождения")
    private LocalDate dateOfBirth;

    @Schema(description = "Пол")
    private Gender gender;
}
