package com.example.vet_pet.model.db.repository;

import com.example.vet_pet.model.db.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.firstName like %:filter% or u.lastName like %:filter%")
    Page<User> findAllFiltered(Pageable pageRequest, @Param("filter") String filter);

    Optional<User> findByEmail(String email);
}
