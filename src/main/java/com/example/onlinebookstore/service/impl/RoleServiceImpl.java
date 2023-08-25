package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.enums.RoleName;
import com.example.onlinebookstore.model.Role;
import com.example.onlinebookstore.repository.RoleRepository;
import com.example.onlinebookstore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(RoleName roleName) {
        return roleRepository.getRoleByRoleName(roleName);
    }
}
