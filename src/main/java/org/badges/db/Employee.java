package org.badges.db;

import lombok.Getter;
import lombok.Setter;
import org.badges.api.domain.EmployeeDto;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
@BatchSize(size = 50)
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String description;

    private String address;

    private String title;

    private String imageUrl;

    @ManyToMany(mappedBy = "toEmployees")
    private Set<BadgeAssignment> badgeAssignments;

    @ManyToOne
    @NotNull
    private Company company;

    private boolean enabled;

    @Override
    public Long getId() {
        return id;
    }


    public EmployeeDto transformToNewsDto() {
        return new EmployeeDto().setId(id)
                .setEmail(email)
                .setImageUrl(imageUrl)
                .setTitle(title)
                .setName(name);
    }
}
