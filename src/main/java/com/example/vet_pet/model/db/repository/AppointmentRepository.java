package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("select u.appointments from User u where u.id = :userId")
    List<Appointment> getUserAppointments(@Param("userId") Long userId);

    @Query("select p.appointments from Pet p where p.id = :petId")
    List<Appointment> getPetAppointments(@Param("petId") Long petId);

}
