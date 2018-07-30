package org.badges.db.campaign;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
//@Entity
//@Where(clause = "deleted=false")
public class Campaign {

    private long id;

    private String name;

    private String userDescription;

    private BadgeCampaignStatus status;

    private BadgeCampaignRule rule;

    private Date startDate;

    private Date endDate;

    private boolean periodic;

    private boolean enabled;

    private boolean deleted;

}
