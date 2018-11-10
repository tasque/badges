package org.badges.db.campaign;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "campaign", schema = "public")
@ToString(of = {"id", "description"})
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "campaign")
    private Set<Badge> badges;

    @OneToMany(mappedBy = "campaign")
    private Set<BadgeAssignment> badgeAssignments;

    private int countPerCampaign;

    private int countToOneUser;

    private int toUsersMax;

    private boolean hiddenBeforeEnd;

    private boolean hiddenAlways;

    private boolean generateResults;

    private boolean renewPeriod;

    private String period;

    private String description;

    private String imageUrl;

    private Date startDate;

    private Date endDate;

    public boolean outOfDate(Date date) {
        return startDate.after(date) || endDate.before(date);
    }
}
