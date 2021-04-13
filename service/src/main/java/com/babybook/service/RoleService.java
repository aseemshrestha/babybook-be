package com.babybook.service;

import com.babybook.email.model.Role;
import com.babybook.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void saveRole(Role role) {
        this.roleRepository.save(role);
    }
}
