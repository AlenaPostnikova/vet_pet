package com.example.vet_pet.service;

import com.example.vet_pet.exeption.CommonBackendException;
import com.example.vet_pet.model.db.entity.Appointment;
import com.example.vet_pet.model.db.entity.MedHistory;
import com.example.vet_pet.model.db.entity.Pet;
import com.example.vet_pet.model.db.repository.MedHistoryRepository;
import com.example.vet_pet.model.dto.request.MedHistoryInfoReq;
import com.example.vet_pet.model.dto.response.AppointmentInfoResp;
import com.example.vet_pet.model.dto.response.MedHistoryInfoResp;
import com.example.vet_pet.model.dto.response.PetInfoResp;
import com.example.vet_pet.model.enums.StatusAppointment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedHistoryService {
    private final ObjectMapper mapper;
    private final MedHistoryRepository medHistoryRepository;
    private final PetService petService;
    private final AppointmentService appointmentService;

    public MedHistory getMedHistoryFromDB(Long id) {
        Optional<MedHistory> optionalMedHistory = medHistoryRepository.findById(id);

        return optionalMedHistory.orElseThrow(()
                -> new CommonBackendException("Medical history not found", HttpStatus.NOT_FOUND));
    }

    public MedHistoryInfoResp getMedHistory(Long id) {
        MedHistory medHistory = getMedHistoryFromDB(id);
        return mapper.convertValue(medHistory, MedHistoryInfoResp.class);
    }

    public MedHistoryInfoResp addMedHistory(MedHistoryInfoReq req) {

        MedHistory medHistory = mapper.convertValue(req, MedHistory.class);

        MedHistory save = medHistoryRepository.save(medHistory); //сохранили в базу данных
        return mapper.convertValue(save, MedHistoryInfoResp.class);
    }

    public MedHistoryInfoResp linkMedHistoryAndPet(Long medHistoryId, Long petId){
        MedHistory medHistoryFromDB = getMedHistoryFromDB(medHistoryId);
        Pet petFromDB = petService.getPetFromDB(petId);

        if (medHistoryFromDB == null || petFromDB == null){
            return MedHistoryInfoResp.builder().build();
        }
        List<MedHistory> medHistories = petFromDB.getMedHistories();

        MedHistory existingMedHistory = medHistories.stream().filter
                (m -> m.getId().equals(medHistoryId)).findFirst().orElse(null);

        if (existingMedHistory != null){
            return mapper.convertValue(existingMedHistory, MedHistoryInfoResp.class);
        }

        medHistories.add(medHistoryFromDB);
        Pet pet = petService.updateLinkList(petFromDB);

        medHistoryFromDB.setPet(pet);
        medHistoryRepository.save(medHistoryFromDB);

        MedHistoryInfoResp medHistoryInfoResp = mapper.convertValue(medHistoryFromDB, MedHistoryInfoResp.class);
        PetInfoResp petInfoResp = mapper.convertValue(petFromDB, PetInfoResp.class);

        medHistoryInfoResp.setPetInfoResp(petInfoResp);

        return medHistoryInfoResp;
    }

    public MedHistoryInfoResp linkMedHistoryAndAppointment(Long medHistoryId, Long appointmentId) {
        MedHistory medHistoryFromDB = getMedHistoryFromDB(medHistoryId);
        Appointment appointmentFromDB = appointmentService.getAppointmentFromDB(appointmentId);

        if (medHistoryFromDB == null || appointmentFromDB == null) {
            return MedHistoryInfoResp.builder().build();
        }

        MedHistory medHistory = appointmentFromDB.getMedHistory();
        MedHistory existingMedHistory = null;

        if (medHistoryId == medHistory.getId()) {
            existingMedHistory = medHistory;
        }

        if (existingMedHistory != null) {
            return mapper.convertValue(existingMedHistory, MedHistoryInfoResp.class);
        }

        appointmentFromDB.setStatus(StatusAppointment.COMPLETED);
        medHistoryFromDB.setAppointment(appointmentFromDB);
        medHistoryRepository.save(medHistoryFromDB);

        MedHistoryInfoResp medHistoryInfoResp = mapper.convertValue(medHistoryFromDB, MedHistoryInfoResp.class);
        AppointmentInfoResp appointmentInfoResp = mapper.convertValue(appointmentFromDB, AppointmentInfoResp.class);

        medHistoryInfoResp.setAppointmentInfoResp(appointmentInfoResp);

        return medHistoryInfoResp;
    }
}
