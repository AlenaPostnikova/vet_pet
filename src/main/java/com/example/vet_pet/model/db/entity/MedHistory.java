package com.example.vet_pet.model.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "medHistory")
public class MedHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pet pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "complaints")
    private String complaints;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "medicine")
    private String medicine;

    @Column(name = "other_info")
    private String otherInfo;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
