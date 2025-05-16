package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.MedHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedHistoryRepository extends JpaRepository<MedHistory, Long> {
}
