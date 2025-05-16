package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.Timetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    Optional<Timetable> getTimetableByDateAndTime(String date, String time);

    @Query("select d.timetables from Doctor d where d.id = :doctorId")
    List<Timetable> getDoctorTimetables(@Param("doctorId") Long doctorId);

    @Query("select t from Timetable t where t.date like %:filter% or t.time like %:filter%")
    Page<Timetable> findAllFiltered(Pageable pageRequest, @Param("filter") String filter);

}
