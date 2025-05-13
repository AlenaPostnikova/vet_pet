package com.example.vet_pet.model.db.entity;

import com.example.vet_pet.model.enums.Gender;
import com.example.vet_pet.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_pet")
    private String namePet;

    @Column(name = "birthday_pet")
    private LocalDate dateOfBirthPet;

    @Column(name = "type_pet")
    private String typePet;

    @Column(name = "breed_pet")
    private String breedPet;

    @Column(name = "color_pet")
    private String colorPet;

    @Column(name = "gender_pet")
    @Enumerated(EnumType.STRING)
    private Gender genderPet;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private List<Appointment> appointments;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private List<MedHistory> medHistories;
}
