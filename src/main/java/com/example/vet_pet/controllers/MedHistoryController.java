package com.example.vet_pet.controllers;

import com.example.vet_pet.model.dto.request.MedHistoryInfoReq;
import com.example.vet_pet.model.dto.response.MedHistoryInfoResp;
import com.example.vet_pet.service.MedHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
