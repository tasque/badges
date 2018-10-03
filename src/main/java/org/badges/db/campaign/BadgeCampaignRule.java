package org.badges.db.campaign;

import lombok.Getter;
import lombok.Setter;
import org.badges.db.Badge;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@Entity
public class BadgeCampaignRule {

    private long id;

    private Badge badge;

    private int countPerCampaign;

    private int countToOneUser;

    private int toUsersMax;

    private boolean hiddenBeforeEnd;

    private boolean hiddenAlways;

    private boolean showResults;

    private String period;

    private Date startDate;

    private Date endDate;
}
