package org.badges.db;

import lombok.Getter;
import lombok.Setter;
import org.badges.api.domain.NewsDto;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Where(clause = "deleted=false")
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "news_type")
    private NewsType newsType;

    @Column(name = "entity_id")
    private Long entityId;

    private String comment;

    @ManyToOne
    private Employee author;

    private String tags;

    @ManyToOne
    @NotNull
    private Company company;

    @BatchSize(size = 20)
    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "employee_news",
            joinColumns = {@JoinColumn(name = "news_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<Employee> toEmployees = Collections.emptySet();

    private boolean deleted;

//    @Any(metaDef = "BaseEntity", metaColumn = @Column(name = "news_type"), fetch = FetchType.LAZY)
//    @JoinColumn(name = "entity_id")
//    private BaseEntity entity;


    @Override
    public Long getId() {
        return id;
    }


    public NewsDto transformToDto() {
        return new NewsDto().setId(id)
                .setAuthor(author != null ? author.transformToDto() : null)
                .setToEmployees(toEmployees.stream().map(Employee::transformToDto).collect(Collectors.toList()))
                .setEntityId(entityId)
                .setNewsType(newsType)
                .setComment(comment);
    }
}
