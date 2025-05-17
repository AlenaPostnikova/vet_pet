package com.example.vet_pet.controllers;

import com.example.vet_pet.model.dto.request.DoctorInfoReq;
import com.example.vet_pet.model.dto.response.DoctorInfoResp;
import com.example.vet_pet.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
@Tag(name = "Ветеринары")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить ветеринара по id")
    public DoctorInfoResp getDoctor(@PathVariable Long id){
        return doctorService.getDoctor(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Создать ветеринара",
            security = @SecurityRequirement(name = AUTHORIZATION))
    public DoctorInfoResp addDoctor(@RequestBody DoctorInfoReq req){
        return doctorService.addDoctor(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные ветеринара по id",
            security = @SecurityRequirement(name = AUTHORIZATION))
    public DoctorInfoResp updateDoctor(@PathVariable Long id, @RequestBody DoctorInfoReq req){
        return doctorService.updateDoctor(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Удалить ветеринара по id",
            security = @SecurityRequirement(name = AUTHORIZATION))
    public void deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех ветеринаров")
    public Page<DoctorInfoResp> getAllDoctors(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "firstName") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter){
        return doctorService.getAllDoctors(page, perPage, sort, order, filter);
    }
}
