package org.badges.db.campaign;

import lombok.Getter;
import lombok.Setter;
import org.badges.db.Badge;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "campaign", schema = "public")
public class Campaign {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "campaign")
    private Set<Badge> badge;

    private int countPerCampaign;

    private int countToOneUser;

    private int toUsersMax;

    private boolean hiddenBeforeEnd;

    private boolean hiddenAlways;

    private boolean generateResults;

    private boolean renewPeriod;

    private String period;

    private Date startDate;

    private Date endDate;

    public boolean outOfDate(Date date) {
        return startDate.before(date) || endDate.after(date);
    }
}
