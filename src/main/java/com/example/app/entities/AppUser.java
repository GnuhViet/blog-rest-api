package com.example.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String fullName;
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    @ManyToMany(fetch =  FetchType.EAGER)
    private Collection<Role> roles;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "appUser",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Article> articles;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "appUser",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Comments> comments;
}
