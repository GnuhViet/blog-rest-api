package com.example.app.service;

import com.example.app.entities.AppUser;
import com.example.app.entities.Role;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
    boolean existByUsername(String username);
    boolean existByEmail();
    boolean existByPhone();
}
