package org.badges.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.Badge;
import org.badges.db.campaign.BadgeCampaignRule;
import org.badges.service.BadgeService;
import org.badges.service.TimeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BadgeRenewalJob implements Job {

    private final BadgeService badgeService;

    private final TimeService timeService;


    @Override
    public void execute(JobExecutionContext context) {
        String badgeIdString = context.getTrigger().getJobDataMap().getString("badgeId");

        log.info("Renew badge with id " + badgeIdString);

        Long badgeId = Long.valueOf(badgeIdString);

        Badge badge = badgeService.getSpecialBadge(badgeId);
        BadgeCampaignRule badgeCampaignRule = badge.getBadgeCampaignRule();

        timeService.fitNextEndDate(badgeCampaignRule);
        badgeService.save(badge);


    }


}
