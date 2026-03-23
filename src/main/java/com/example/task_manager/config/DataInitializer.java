package com.example.task_manager.config;

import com.example.task_manager.entity.Role;
import com.example.task_manager.enums.RoleName;
import com.example.task_manager.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            roleRepository.save(new Role(null, RoleName.USER));
        }
        if (roleRepository.findByName(RoleName.MANAGER).isEmpty()) {
            roleRepository.save(new Role(null, RoleName.MANAGER));
        }
    }
}
