package org.badges.db;

import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NewsType newsType;

    private String comment;

    @ManyToOne
    private Employee author;

    private String tags;

    @ManyToOne
    private Company company;

    @BatchSize(size = 20)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "employee_news",
            joinColumns = {@JoinColumn(name = "news_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<Employee> toEmployees;

    @ManyToOne
    private Badge badge;
}
