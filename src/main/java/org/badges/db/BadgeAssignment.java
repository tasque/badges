package org.badges.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

@Entity
@Getter
@Setter
public class BadgeAssignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Badge badge;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "employee_badge_assignment",
            joinColumns = {@JoinColumn(name = "badge_assignment_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<Employee> toEmployees;

    @ManyToOne
    private Employee assigner;

    private String comment;

    private String tags;

    @ManyToOne
    private News news;

    @Override
    public Long getId() {
        return id;
    }
}
