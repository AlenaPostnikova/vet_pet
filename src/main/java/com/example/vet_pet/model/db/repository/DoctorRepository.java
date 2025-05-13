package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    @Query("select d from Doctor d where d.firstName like %:filter% or d.specDoctor like %:filter%")
    Page<Doctor> findAllFiltered(Pageable pageRequest, @Param("filter") String filter);
}
