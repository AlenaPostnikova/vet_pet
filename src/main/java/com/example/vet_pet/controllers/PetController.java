package com.example.vet_pet.controllers;


import com.example.vet_pet.model.dto.request.PetInfoReq;
import com.example.vet_pet.model.dto.response.PetInfoResp;
import com.example.vet_pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
@Tag(name = "Питомцы")
public class PetController {
    private final PetService petService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить питомца по id")
    public PetInfoResp getPet(@PathVariable Long id){
        return petService.getPet(id);
    }

    @PostMapping
    @Operation(summary = "Добавить питомца")
    public PetInfoResp addPet(@RequestBody PetInfoReq petInfoReq){
        return petService.addPet(petInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные питомца по id")
    public PetInfoResp updatePet(@PathVariable Long id, @RequestBody PetInfoReq req){
        return petService.updatePet(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить питомца по id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deletePet(@PathVariable Long id){
        petService.deletePet(id);
    }


    @PostMapping("/linkPetAndUser/{petId}/{userId}")
    @Operation(summary = "Прикрепить питомца с id к пользователю с id")
    public PetInfoResp linkPetAndUser(@PathVariable Long petId, @PathVariable Long userId) {
        return petService.linkPetAndUser(petId, userId);
    }

    @GetMapping("/{id}/myPets")
    @Operation(summary = "Получить список питомцев пользователя по id")
    public List<PetInfoResp> getMyPets(@PathVariable Long id){
        return petService.getUserPets(id);
    }
}
