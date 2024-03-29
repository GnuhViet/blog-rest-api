package com.example.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "nvarchar(255)")
    private String title;

    private String thumbnail;

    @Column(columnDefinition = "nvarchar(255)")
    private String shortDescription;

    @Column(columnDefinition = "nvarchar(255)")
    private String content;

    private Date createDate;

    private Date modifiedDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "create_by_app_user_id")
    private AppUser appUser;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
    //https://stackoverflow.com/questions/73078524/java-lang-stackoverflowerror-while-saving-jpa-entities

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "article",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Comment> comments;
}
