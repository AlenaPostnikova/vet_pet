package com.example.vet_pet.service;

import com.example.vet_pet.exeption.CommonBackendException;
import com.example.vet_pet.model.db.entity.*;
import com.example.vet_pet.model.db.repository.AppointmentRepository;
import com.example.vet_pet.model.dto.response.AppointmentInfoResp;
import com.example.vet_pet.model.dto.response.PetInfoResp;
import com.example.vet_pet.model.dto.response.TimetableInfoResp;
import com.example.vet_pet.model.dto.response.UserInfoResp;
import com.example.vet_pet.model.enums.StatusAppointment;
import com.example.vet_pet.model.enums.StatusTimetable;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final ObjectMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final TimetableService timetableService;
    private final UserService userService;
    private final PetService petService;

    public Appointment getAppointmentFromDB(Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        return optionalAppointment.orElseThrow(()
                -> new CommonBackendException("Appointment not found", HttpStatus.NOT_FOUND));
    }

    public AppointmentInfoResp linkAppointmentAndTimetable(Long appointmentId, Long timetableId) {
        Appointment appointmentFromDB = getAppointmentFromDB(appointmentId);
        Timetable timetableFromDB = timetableService.getTimetableFromDB(timetableId);

        if (appointmentFromDB == null || timetableFromDB == null) {
            return AppointmentInfoResp.builder().build();
        }

        Appointment appointment = timetableFromDB.getAppointment();
        Appointment existingAppointment = null;

        if (appointmentId == appointment.getId()) {
            existingAppointment = appointment;
        }

        if (existingAppointment != null) {
            return mapper.convertValue(existingAppointment, AppointmentInfoResp.class);
        }

        timetableFromDB.setStatusTimetable(StatusTimetable.BUSY);
        appointmentFromDB.setTimetable(timetableFromDB);
        appointmentRepository.save(appointmentFromDB);

        AppointmentInfoResp appointmentInfoResp = mapper.convertValue(appointmentFromDB, AppointmentInfoResp.class);
        TimetableInfoResp timetableInfoResp = mapper.convertValue(timetableFromDB, TimetableInfoResp.class);

        appointmentInfoResp.setTimetableInfoResp(timetableInfoResp);

        return appointmentInfoResp;
    }

    public AppointmentInfoResp linkAppointmentAndUser(Long appointmentId, Long userId){
        Appointment appointmentFromDB = getAppointmentFromDB(appointmentId);
        User userFromDB = userService.getUserFromDB(userId);

        if (appointmentFromDB == null || userFromDB == null){
            return AppointmentInfoResp.builder().build();
        }

        List<Appointment> appointments = userFromDB.getAppointments();

        Appointment existingAppointment = appointments.stream().filter
                (a -> a.getId().equals(appointmentId)).findFirst().orElse(null);

        if (existingAppointment != null){
            return mapper.convertValue(existingAppointment, AppointmentInfoResp.class);
        }

        appointments.add(appointmentFromDB);
        User user = userService.updateLinkList(userFromDB);

        appointmentFromDB.setUser(user);
        appointmentRepository.save(appointmentFromDB);

        AppointmentInfoResp appointmentInfoResp = mapper.convertValue(appointmentFromDB, AppointmentInfoResp.class);
        UserInfoResp userInfoResp = mapper.convertValue(userFromDB, UserInfoResp.class);

        appointmentInfoResp.setUserInfoResp(userInfoResp);

        return appointmentInfoResp;
    }

    public AppointmentInfoResp linkAppointmentAndPet(Long appointmentId, Long petId){
        Appointment appointmentFromDB = getAppointmentFromDB(appointmentId);
        Pet petFromDB = petService.getPetFromDB(petId);

        if (appointmentFromDB == null || petFromDB == null){
            return AppointmentInfoResp.builder().build();
        }

        List<Appointment> appointments = petFromDB.getAppointments();

        Appointment existingAppointment = appointments.stream().filter
                (a -> a.getId().equals(appointmentId)).findFirst().orElse(null);

        if (existingAppointment != null){
            return mapper.convertValue(existingAppointment, AppointmentInfoResp.class);
        }

        appointments.add(appointmentFromDB);
        Pet pet = petService.updateLinkList(petFromDB);

        appointmentFromDB.setPet(pet);
        appointmentRepository.save(appointmentFromDB);

        AppointmentInfoResp appointmentInfoResp = mapper.convertValue(appointmentFromDB, AppointmentInfoResp.class);
        PetInfoResp petInfoResp = mapper.convertValue(petFromDB, PetInfoResp.class);

        appointmentInfoResp.setPetInfoResp(petInfoResp);

        return appointmentInfoResp;
    }

    public List<AppointmentInfoResp> getUserAppointments(Long userId){

        User user = userService.getUserFromDB(userId);

        return appointmentRepository.getUserAppointments(userId).stream()
                .map(a -> mapper.convertValue(a, AppointmentInfoResp.class))
                .collect(Collectors.toList());
    }

    public List<AppointmentInfoResp> getPetAppointments(Long petId){

        Pet pet = petService.getPetFromDB(petId);

        return appointmentRepository.getPetAppointments(petId).stream()
                .map(a -> mapper.convertValue(a, AppointmentInfoResp.class))
                .collect(Collectors.toList());
    }


    public void cancelAppointment(Long id){
        Appointment appointment = getAppointmentFromDB(id);
        appointment.setStatus(StatusAppointment.CANCEL);
        appointment = appointmentRepository.save(appointment);
    }
}
