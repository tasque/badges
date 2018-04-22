package org.badges.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String address;

    private String title;

    private String imageUrl;

    @ManyToMany(mappedBy = "toEmployees", fetch = FetchType.LAZY)
    private Set<BadgeAssignment> badgeAssignments;
}
