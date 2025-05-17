package com.example.vet_pet.service;

import com.example.vet_pet.exeption.CommonBackendException;
import com.example.vet_pet.model.db.entity.Doctor;
import com.example.vet_pet.model.db.entity.Timetable;
import com.example.vet_pet.model.db.repository.TimetableRepository;
import com.example.vet_pet.model.dto.request.TimetableInfoReq;
import com.example.vet_pet.model.dto.response.DoctorInfoResp;
import com.example.vet_pet.model.dto.response.TimetableInfoResp;
import com.example.vet_pet.model.enums.StatusTimetable;
import com.example.vet_pet.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j //для логирования
@Service
@RequiredArgsConstructor //созд. конструктора для инициализации бина
public class TimetableService {
    private final ObjectMapper mapper;
    private final TimetableRepository timetableRepository;
    private final DoctorService doctorService;


    public void isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new CommonBackendException("Invalid date", HttpStatus.BAD_REQUEST);
        }
    }

    public void isValidTime(String time) {
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new CommonBackendException("Invalid time", HttpStatus.BAD_REQUEST);
        }
    }


    public Timetable getTimetableFromDB(Long id) {
        Optional<Timetable> optionalTimetable = timetableRepository.findById(id);

        return optionalTimetable.orElseThrow(() ->
                new CommonBackendException("Timetable not found", HttpStatus.NOT_FOUND));
    }

    public void deleteTimetable(Long id) {
        Timetable timetableFromDB = getTimetableFromDB(id);
        timetableRepository.deleteById(id);
    }

    public TimetableInfoResp getTimetable(Long id) {
        Timetable timetable = getTimetableFromDB(id);
        return mapper.convertValue(timetable, TimetableInfoResp.class);
    }

    public TimetableInfoResp addTimetable(TimetableInfoReq req) {

        timetableRepository.getTimetableByDateAndTime(req.getDate(), req.getTime()).ifPresent(t -> { //проверка нет ли такой записи расписания
            throw new CommonBackendException("Timetable already exists", HttpStatus.CONFLICT);
        });

        isValidDate(req.getDate());
        isValidTime(req.getTime());

        Timetable timetable = mapper.convertValue(req, Timetable.class);
        timetable.setStatusTimetable(StatusTimetable.EMPTY);

        Timetable saveTimetable = timetableRepository.save(timetable);
        return mapper.convertValue(saveTimetable, TimetableInfoResp.class);
    }

    public TimetableInfoResp updateTimetable(Long id, TimetableInfoReq req) {
        Timetable timetableFromDB = getTimetableFromDB(id);

        isValidDate(req.getDate());
        isValidTime(req.getTime());

        Timetable timetableReq = mapper.convertValue(req, Timetable.class);

        timetableFromDB.setDate(req.getDate() == null ? timetableFromDB.getDate() : req.getDate());
        timetableFromDB.setTime(req.getTime() == null ? timetableFromDB.getTime() : req.getTime());

        timetableFromDB = timetableRepository.save(timetableFromDB);
        return mapper.convertValue(timetableFromDB, TimetableInfoResp.class);
    }


    public TimetableInfoResp linkTimetableAndDoctor(Long timetableId, Long doctorId){
        Timetable timetableFromDB = getTimetableFromDB(timetableId);
        Doctor doctorFromDB = doctorService.getDoctorFromDB(doctorId);

        if (timetableFromDB == null || doctorFromDB == null){
            return TimetableInfoResp.builder().build();
        }

        List<Timetable> timetables = doctorFromDB.getTimetables();

        Timetable existingTimetable = timetables.stream().filter
                (t -> t.getId().equals(timetableId)).findFirst().orElse(null);

        if (existingTimetable != null){
            return mapper.convertValue(existingTimetable, TimetableInfoResp.class);
        }

        timetableFromDB.setStatusTimetable(StatusTimetable.FREE);

        timetables.add(timetableFromDB);
        Doctor doctor = doctorService.updateLinkList(doctorFromDB);

        timetableFromDB.setDoctor(doctor);
        timetableRepository.save(timetableFromDB);

        TimetableInfoResp timetableInfoResp = mapper.convertValue(timetableFromDB, TimetableInfoResp.class);
        DoctorInfoResp doctorInfoResp = mapper.convertValue(doctorFromDB, DoctorInfoResp.class);

        timetableInfoResp.setDoctorInfoResp(doctorInfoResp);

        return timetableInfoResp;
    }

    public List<TimetableInfoResp> getDoctorTimetables(Long doctorId){

        Doctor doctor = doctorService.getDoctorFromDB(doctorId);

        return timetableRepository.getDoctorTimetables(doctorId).stream()
                .map(t-> mapper.convertValue(t, TimetableInfoResp.class))
                .collect(Collectors.toList());
    }

    public Page<TimetableInfoResp> getAllFreeTimetables
            (Integer page, Integer perPage, String sort, Sort.Direction order, String filter){

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);

        Page<Timetable> timetables;

        if(StringUtils.hasText(filter)){
            timetables = timetableRepository.findAllFiltered(pageRequest, filter);
        } else {
            timetables = timetableRepository.findAllFree(pageRequest);
        }

        List<TimetableInfoResp> content = timetables.getContent().stream()
                .map(t -> mapper.convertValue(t, TimetableInfoResp.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, timetables.getTotalElements());
    }

}
