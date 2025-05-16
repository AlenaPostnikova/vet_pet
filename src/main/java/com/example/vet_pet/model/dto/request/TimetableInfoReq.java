package com.example.vet_pet.model.dto.request;

import com.example.vet_pet.model.enums.StatusTimetable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class TimetableInfoReq {

    @NotNull
    @Schema(description = "Дата записи расписания")
    private String date;

    @NotNull
    @Schema(description = "Время записи расписания")
    private String time;

    @NotNull
    @Schema(description = "Статус записи расписания")
    private StatusTimetable statusTimetable;

}
