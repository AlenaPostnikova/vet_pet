package com.example.vet_pet.service;

import com.example.vet_pet.model.db.entity.Doctor;
import com.example.vet_pet.model.dto.request.DoctorInfoReq;
import com.example.vet_pet.exeption.CommonBackendException;
import com.example.vet_pet.model.db.repository.DoctorRepository;
import com.example.vet_pet.model.dto.response.DoctorInfoResp;
import com.example.vet_pet.model.enums.Roles;
import com.example.vet_pet.model.enums.Status;
import com.example.vet_pet.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {
    private final ObjectMapper mapper;
    private final DoctorRepository doctorRepository;

    public DoctorInfoResp addDoctor(DoctorInfoReq req) {

        if (!EmailValidator.getInstance().isValid(req.getEmail())) {
            throw new CommonBackendException("Email invalid", HttpStatus.BAD_REQUEST);
        }

        doctorRepository.findByEmail(req.getEmail()).ifPresent(d -> {
            throw new CommonBackendException("Doctor already exists", HttpStatus.CONFLICT);
        });

        Doctor doctor = mapper.convertValue(req, Doctor.class);
        doctor.setStatus(Status.CREATED);
        doctor.setRole(Roles.ROLE_DOCTOR);

        Doctor saveDoctor = doctorRepository.save(doctor);
        return mapper.convertValue(saveDoctor, DoctorInfoResp.class);
    }


    public DoctorInfoResp getDoctor(Long id) {
        Doctor doctor = getDoctorFromDB(id);
        return mapper.convertValue(doctor, DoctorInfoResp.class);
    }

    public Doctor getDoctorFromDB(Long id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);

        final String errMsg = String.format("Doctor with id: %s not found", id);

        return optionalDoctor.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    public DoctorInfoResp updateDoctor(Long id, DoctorInfoReq req) {
        Doctor doctorFromDB = getDoctorFromDB(id);

        Doctor doctorReq = mapper.convertValue(req, Doctor.class);

        doctorFromDB.setEmail(doctorReq.getEmail() == null ? doctorFromDB.getEmail() : doctorReq.getEmail());
        doctorFromDB.setNumPhone(doctorReq.getNumPhone() == null ? doctorFromDB.getNumPhone() : doctorReq.getNumPhone());
        doctorFromDB.setPassword(doctorReq.getPassword() == null ? doctorFromDB.getPassword() : doctorReq.getPassword());
        doctorFromDB.setFirstName(doctorReq.getFirstName() == null ? doctorFromDB.getFirstName() : doctorReq.getFirstName());
        doctorFromDB.setLastName(doctorReq.getLastName() == null ? doctorFromDB.getLastName() : doctorReq.getLastName());
        doctorFromDB.setMiddleName(doctorReq.getMiddleName() == null ? doctorFromDB.getMiddleName() : doctorReq.getMiddleName());
        doctorFromDB.setDateOfBirth(doctorReq.getDateOfBirth() == null ? doctorFromDB.getDateOfBirth() : doctorReq.getDateOfBirth());
        doctorFromDB.setSpecDoctor(doctorReq.getSpecDoctor() == null ? doctorFromDB.getSpecDoctor() : doctorReq.getSpecDoctor());
        doctorFromDB.setGender(doctorReq.getGender() == null ? doctorFromDB.getGender() : doctorReq.getGender());

        doctorFromDB.setStatus(Status.UPDATED);
        doctorFromDB = doctorRepository.save(doctorFromDB);
        return mapper.convertValue(doctorFromDB, DoctorInfoResp.class);
    }


    public void deleteDoctor(Long id) {
        Doctor doctorFromDB = getDoctorFromDB(id);

        doctorFromDB.setStatus(Status.DELETED);
        doctorFromDB = doctorRepository.save(doctorFromDB);
    }

    public Doctor updateLinkList(Doctor updatedDoctor) {
        return doctorRepository.save(updatedDoctor);
    }

    public Page<DoctorInfoResp> getAllDoctors
            (Integer page, Integer perPage, String sort, Sort.Direction order, String filter){

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);

        Page<Doctor> doctors;

        if(StringUtils.hasText(filter)){
            doctors = doctorRepository.findAllFiltered(pageRequest, filter);
        } else {
            doctors = doctorRepository.findAll(pageRequest);
        }

        List<DoctorInfoResp> content = doctors.getContent().stream()
                .map(d -> mapper.convertValue(d, DoctorInfoResp.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, doctors.getTotalElements());
    }

}
