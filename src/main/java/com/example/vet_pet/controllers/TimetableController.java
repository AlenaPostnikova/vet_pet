package com.example.vet_pet.controllers;

import com.example.vet_pet.model.dto.request.TimetableInfoReq;
import com.example.vet_pet.model.dto.response.TimetableInfoResp;
import com.example.vet_pet.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetable")
@RequiredArgsConstructor
@Tag(name = "Расписание")
public class TimetableController {
    private final TimetableService timetableService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись расписания по id")
    public TimetableInfoResp getTimetable(@PathVariable Long id){
        return timetableService.getTimetable(id);
    }

    @PostMapping
    @Operation(summary = "Создать запись расписания")
    public TimetableInfoResp addUserTimetable(@RequestBody TimetableInfoReq req){
        return timetableService.addTimetable(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные записи расписания по id")
    public TimetableInfoResp updateTimetable(@PathVariable Long id, @RequestBody TimetableInfoReq req){
        return timetableService.updateTimetable(id, req);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Удалить запись расписания по id")
    public void deleteTimetable(@PathVariable Long id){
        timetableService.deleteTimetable(id);
    }


    @PostMapping("/linkTimetableAndDoctor/{timetableId}/{doctorId}")
    @Operation(summary = "Прикрепить запись расписания с id к доктору с id")
    public TimetableInfoResp linkTimetableAndDoctor(@PathVariable Long timetableId, @PathVariable Long doctorId) {
        return timetableService.linkTimetableAndDoctor(timetableId, doctorId);
    }

    @GetMapping("/{id}/MyTimetable")
    @Operation(summary = "Получить расписание доктора по id")
    public List<TimetableInfoResp> getDoctorATimetable(@PathVariable Long id){
        return timetableService.getDoctorTimetables(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех записей расписания")
    public Page<TimetableInfoResp> getAllTimetables(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer perPage,
                                                    @RequestParam(defaultValue = "date") String sort,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                    @RequestParam(required = false) String filter){
        return timetableService.getAllFreeTimetables(page, perPage, sort, order, filter);
    }

}
