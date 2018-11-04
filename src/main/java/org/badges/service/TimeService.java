package org.badges.service;

import org.badges.db.campaign.Campaign;
import org.joda.time.LocalDateTime;
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


    public Period fitNextEndDate(Campaign campaign) {
        Period period = periodFormatter.parsePeriod(campaign.getPeriod());
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDate = new LocalDateTime(campaign.getStartDate());
        LocalDateTime endDate = new LocalDateTime(campaign.getEndDate());
        while (endDate.isBefore(now))
        {
            startDate = startDate.plus(period);
            endDate = endDate.plus(period);
        }

        campaign.setStartDate(startDate.toDate());
        campaign.setEndDate(endDate.toDate());

        return period;
    }
}
