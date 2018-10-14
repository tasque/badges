package org.badges.service;

import org.badges.db.campaign.BadgeCampaignRule;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimeService {


    private final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendYears().appendSuffix("y")
            .appendMonths().appendSuffix("m")
            .appendWeeks().appendSuffix("w")
            .appendDays().appendSuffix("d")
            .appendHours().appendSuffix("h").toFormatter();


    public Period fitNextEndDate(BadgeCampaignRule badgeCampaignRule) {
        Period period = periodFormatter.parsePeriod(badgeCampaignRule.getPeriod());

        DateTime startDate = new DateTime(badgeCampaignRule.getStartDate());
        DateTime endDate = new DateTime(badgeCampaignRule.getEndDate());
        while (endDate.isBeforeNow())
        {
            startDate = startDate.plus(period);
            endDate = endDate.plus(period);
        }

        badgeCampaignRule.setStartDate(startDate.toDate());
        badgeCampaignRule.setEndDate(endDate.toDate());

        return period;
    }
}
