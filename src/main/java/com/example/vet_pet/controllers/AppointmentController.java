package com.example.vet_pet.controllers;

import com.example.vet_pet.model.dto.response.AppointmentInfoResp;
import com.example.vet_pet.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
@Tag(name = "Запись на прием")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/linkAppointmentAndTimetable/{appointmentId}/{timetableId}")
    @Operation(summary = "Связать запись на прием и пустую запись расписания")
    public AppointmentInfoResp linkAppointmentAndTimetable(@PathVariable Long appointmentId, @PathVariable Long timetableId) {
        return appointmentService.linkAppointmentAndTimetable(appointmentId, timetableId);
    }

    @PostMapping("/linkAppointmentAndUser/{appointmentId}/{userId}")
    @Operation(summary = "Связать запись на прием и пользователя")
    public AppointmentInfoResp linkAppointmentAndUser(@PathVariable Long appointmentId, @PathVariable Long userId) {
        return appointmentService.linkAppointmentAndUser(appointmentId, userId);
    }

    @PostMapping("/linkAppointmentAndPet/{appointmentId}/{petId}")
    @Operation(summary = "Связать запись на прием и питомца")
    public AppointmentInfoResp linkAppointmentAndPet(@PathVariable Long appointmentId, @PathVariable Long petId) {
        return appointmentService.linkAppointmentAndPet(appointmentId, petId);
    }

    @GetMapping("/{id}/myAppointments")
    @Operation(summary = "Получить список записей на прием пользователя по id")
    public List<AppointmentInfoResp> getUserAppointment(@PathVariable Long id){
        return appointmentService.getUserAppointments(id);
    }

    @GetMapping("/{id}/myPetAppointments")
    @Operation(summary = "Получить список записей на прием питомца по id")
    public List<AppointmentInfoResp> getPetAppointment(@PathVariable Long id){
        return appointmentService.getPetAppointments(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отменить запись на прием по id")
    public void cancelAppointment(@PathVariable Long id){
        appointmentService.cancelAppointment(id);
    }

}