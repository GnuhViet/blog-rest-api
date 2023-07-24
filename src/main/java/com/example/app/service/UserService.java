package com.example.app.service;

import com.example.app.dto.AppUserDto;
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

    private AppUser getUser(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        // log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        AppUser user = getUser(username);
        Role role = roleRepository.findByName(roleName).orElseThrow();

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
    }

    public List<AppUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(obj -> modelMapper.map(obj, AppUserDto.class))
                .collect(Collectors.toList());
    }

    public List<AppUserDto> getAllUsers(Pageable page) {
        List<AppUser> res = userRepository.findAll(page).getContent();
        return userRepository.findAll(page).getContent()
                .stream()
                .map(obj -> modelMapper.map(obj, AppUserDto.class))
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

