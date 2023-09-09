package com.example.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(columnDefinition = "nvarchar(255)") private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "article_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private Set<Article> articles;
}
