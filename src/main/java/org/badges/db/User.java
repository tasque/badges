package org.badges.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
@Table(name = "user", schema = "public")
@BatchSize(size = 50)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nativeName;

    private String email;

    private String description;

    private String address;

    private String title;

    private String imageUrl;

    @ManyToMany(mappedBy = "toUsers")
    private Set<BadgeAssignment> badgeAssignments;

    @ManyToMany
    @JoinTable(name = "user_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_user_role_id")
    )
    private Set<UserRole> userRoles = Collections.emptySet();

    private boolean enabled;

    private Date dateOfBirth;

    private String messenger;

    private boolean admin;

    public boolean isDisabled() {
        return !enabled;
    }

}
