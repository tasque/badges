package org.badges.db.campaign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeCampaignRule {

    private int countPerCampaign = -1;

    private boolean hiddenBeforeEnd;

    private boolean hiddenAlways;
}
