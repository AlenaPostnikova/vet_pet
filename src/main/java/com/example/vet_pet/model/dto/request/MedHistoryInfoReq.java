package com.example.vet_pet.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class MedHistoryInfoReq {

    @NotNull
    @Schema(description = "Температура тела")
    private float temperature;

    @NotEmpty
    @Schema(description = "Жалобы")
    private String complaints;

    @NotEmpty
    @Schema(description = "Диагноз")
    private String diagnosis;

    @Schema(description = "Назначенные препараты")
    private String medicine;

    @Schema(description = "Прочая информация")
    private String otherInfo;
}
