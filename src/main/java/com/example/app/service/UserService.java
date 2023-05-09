package com.example.app.service;

import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.repository.RoleRepository;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = getUser(username);

        if (user == null) {
            // log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public AppUser saveUser(AppUser user) {
        // log.info("Saving new user {} to the database", user.getUsername());
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        // log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        // log.info("Adding role {} to user {}", roleName, username);
        AppUser user = getUser(username);
        Role role = roleRepository.findByName(roleName).orElseThrow();

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
    }

    public AppUser getUser(String username) throws UsernameNotFoundException {
        // log.info("Fetching user {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<AppUser> getAllUsers() {
        // log.info("Fetching all user");
        return userRepository.findAll();
    }

    public List<AppUser> getAllUsers(Pageable page) {
        // log.info("Fetching all user");
        List<AppUser> res = userRepository.findAll(page).getContent();
        return res;
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existByEmail() {
        return false;
    }

    public boolean existByPhone() {
        return false;
    }
}

