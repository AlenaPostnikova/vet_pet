package com.example.vet_pet.model.enums;

import lombok.Getter;
import lombok.Setter;

public enum SpecializationDoctor {

    THERAPIST ("терапевт"),
    CARDIOLOGIST ("кардиолог"),
    OPHTHALMOLOGIST ("офтальмолог"),
    SURGEON ("хирург"),
    ANESTHESIOLOGIST ("анастезиолог"),
    ORTHOPEDIC_SURGEON ("ортопед"),
    NEUROLOGIST ("невролог"),
    DERMATOLOGIST ("дерматолог"),
    GASTROENTEROLOGIST ("гастроэнтеролог"),
    ONCOLOGIST ("онколог");

    private String specialization;

    SpecializationDoctor(String specialization) {
        this.specialization = specialization;
    }


}
