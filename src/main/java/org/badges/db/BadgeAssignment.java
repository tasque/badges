package org.badges.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.badges.db.campaign.Campaign;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "comment"})
public class BadgeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Badge badge;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_badge_assignment",
            joinColumns = {@JoinColumn(name = "badge_assignment_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> toUsers;

    @ManyToOne
    private User assigner;

    @ManyToOne
    private Campaign campaign;

    private String comment;

    private String tags;

    private Date date;

    @ManyToOne
    private News news;

}
