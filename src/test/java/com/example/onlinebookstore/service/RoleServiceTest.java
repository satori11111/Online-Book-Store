package com.example.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.enums.RoleName;
import com.example.onlinebookstore.model.Role;
import com.example.onlinebookstore.repository.RoleRepository;
import com.example.onlinebookstore.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    private static Role role;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setRoleName(RoleName.ROLE_USER);
        role.setId(1L);
    }

    @Test
    public void getRoleByRoleName_validName_returnsRole() {
        when(roleRepository.getRoleByRoleName(any(RoleName.class))).thenReturn(role);

        assertEquals(role, roleService.getRoleByRoleName(RoleName.ROLE_USER));
        verify(roleRepository).getRoleByRoleName(any(RoleName.class));
    }
}
