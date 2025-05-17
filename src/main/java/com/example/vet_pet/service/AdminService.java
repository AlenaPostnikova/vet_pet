package com.example.vet_pet.service;

import com.example.vet_pet.model.db.entity.Admin;
import com.example.vet_pet.model.db.repository.AdminRepository;
import com.example.vet_pet.model.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public void addAdmin() {

        Admin admin = new Admin();
        admin.setLogin("Admin");
        admin.setPassword("admin1");
        admin.setRole(Roles.ROLE_ADMIN);

        Admin save = adminRepository.save(admin);
    }
}
