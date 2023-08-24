package com.example.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
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
    private String avatar;
    @Column(unique = true)
    private String phoneNumber;
    @ManyToMany(fetch =  FetchType.LAZY)
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
    private List<Comment> comments;
}
