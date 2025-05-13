package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("select u.pets from User u where u.id = :userId")
    List<Pet> getUserPets(@Param("userId") Long userId);


}

