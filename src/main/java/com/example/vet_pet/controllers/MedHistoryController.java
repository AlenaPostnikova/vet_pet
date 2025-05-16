package com.example.vet_pet.controllers;

import com.example.vet_pet.model.dto.request.MedHistoryInfoReq;
import com.example.vet_pet.model.dto.response.MedHistoryInfoResp;
import com.example.vet_pet.model.dto.response.PetInfoResp;
import com.example.vet_pet.service.MedHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medHistory")
@RequiredArgsConstructor
@Tag(name = "Истории болезни питомцев")
public class MedHistoryController {
    private final MedHistoryService medHistoryService;

    @PostMapping
    @Operation(summary = "Создать историю болезни питомца")
    public MedHistoryInfoResp addMedHistory(@RequestBody MedHistoryInfoReq req){
        return medHistoryService.addMedHistory(req);
    }

    @PostMapping("/linkMedHistoryAndPet/{medHistoryId}/{petId}")
    @Operation(summary = "Прикрепить историю болезни к питомцу")
    public MedHistoryInfoResp linkMedHistoryAndPet(@PathVariable Long medHistoryId, @PathVariable Long petId) {
        return medHistoryService.linkMedHistoryAndPet(medHistoryId, petId);
    }

    @PostMapping("/linkMedHistoryAndAppointment/{medHistoryId}/{appointmentId}")
    @Operation(summary = "Прикрепить историю бозени к записи на прием")
    public MedHistoryInfoResp linkMedHistoryAndAppointment(@PathVariable Long medHistoryId, @PathVariable Long appointmentId) {
        return medHistoryService.linkMedHistoryAndAppointment(medHistoryId, appointmentId);
    }

    @GetMapping("/{id}/myPetMedHistory")
    @Operation(summary = "Получить всю историю болезни питомца по id")
    public List<MedHistoryInfoResp> getMyPetMedHistory(@PathVariable Long id) {
        return medHistoryService.getMyPetMedHistory(id);
    }

}
