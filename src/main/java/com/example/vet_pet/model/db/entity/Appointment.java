package com.example.vet_pet.model.db.entity;

import com.example.vet_pet.model.enums.StatusAppointment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private StatusAppointment status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "user_appointments")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "pet_appointments")
    private Pet pet;

    @OneToOne(mappedBy = "appointment")
    private MedHistory medHistory;

}
