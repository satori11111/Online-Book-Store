package com.example.onlinebookstore.service;

import com.example.onlinebookstore.enums.RoleName;
import com.example.onlinebookstore.model.Role;

public interface RoleService {
    Role getRoleByRoleName(RoleName roleName);
}
