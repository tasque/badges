package org.badges.job;

import lombok.RequiredArgsConstructor;
import org.badges.db.Badge;
import org.badges.db.campaign.BadgeCampaignRule;
import org.badges.service.BadgeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeRenewalJob implements Job {

    private final BadgeService badgeService;

//    private final BadgeAssignmentService badgeAssignmentService;




    @Override
    public void execute(JobExecutionContext context) {
        long badgeId = context.getTrigger().getJobDataMap().getLong("badgeId");
        Badge badge = badgeService.getSpecialBadge(badgeId);
        BadgeCampaignRule badgeCampaignRule = badge.getBadgeCampaignRule();

        badgeService.rescheduleBadgeRenewal(badgeCampaignRule);
    }


}
