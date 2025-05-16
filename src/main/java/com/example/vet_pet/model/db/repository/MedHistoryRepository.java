package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.MedHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedHistoryRepository extends JpaRepository<MedHistory, Long> {

    @Query("select p.medHistories from Pet p where p.id = :petId")
    List<MedHistory> getPetMedHistory(@Param("petId") Long petId);

}
