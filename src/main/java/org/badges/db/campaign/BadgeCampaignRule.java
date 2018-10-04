package org.badges.db.campaign;

import lombok.Getter;
import lombok.Setter;
import org.badges.db.Badge;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "badge_rule", schema = "public")
public class BadgeCampaignRule {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @PrimaryKeyJoinColumn
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
