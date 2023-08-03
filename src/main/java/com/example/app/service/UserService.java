package com.example.app.service;

import com.example.app.api.model.user.UserProfileRequest;
import com.example.app.dto.appuser.DetailsAppUserDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.repository.RoleRepository;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username, "Username must not be null");

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    private AppUser loadAppUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username, "Username must not be null");

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        return user;
    }

    public <T> T getUserDto(String username, Class<T> dtoType) {
        return modelMapper.map(loadAppUserByUsername(username), dtoType);
    }

    public DetailsAppUserDTO saveUser(AppUser user) {
        Objects.requireNonNull(user, "User must not be null");

        return modelMapper.map(userRepository.save(user), DetailsAppUserDTO.class);
    }

    public DetailsAppUserDTO updateUserProfile(UserProfileRequest userProfile, String username) {
        Objects.requireNonNull(userProfile, "Profile must not be null");
        Objects.requireNonNull(username, "Username must not be null");
        AppUser user = loadAppUserByUsername(username);
        modelMapper.map(userProfile, user);
        return modelMapper.map(user, DetailsAppUserDTO.class);
    }

    public DetailsAppUserDTO updateUserPassword(String newPassword, String username) {
        Objects.requireNonNull(newPassword, "Password must not be null");
        Objects.requireNonNull(username, "Username must not be null");
        AppUser user = loadAppUserByUsername(username);
        user.setPassword(newPassword);
        return modelMapper.map(user, DetailsAppUserDTO.class);
    }

    public Role saveRole(Role role) {
        // log.info("Saving new role {} to the database", role.getName());
        Objects.requireNonNull(role, "Role must not be null");

        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(roleName, "Role name must not be null");


        AppUser user = loadAppUserByUsername(username);
        Role role = roleRepository.findByName(roleName).orElseThrow();

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
    }

    public List<DetailsAppUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(obj -> modelMapper.map(obj, DetailsAppUserDTO.class))
                .collect(Collectors.toList());
    }

    public <T> List<T> getAllUsers(Pageable page, Class<T> dtoType) {
        List<AppUser> res = userRepository.findAll(page).getContent();
        return userRepository.findAll(page).getContent()
                .stream()
                .map(obj -> modelMapper.map(obj, dtoType))
                .collect(Collectors.toList());
    }

    public long countUser() {
        return userRepository.count();
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

