package org.badges.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.CampaignRepository;
import org.badges.service.BadgeService;
import org.badges.service.NewsService;
import org.badges.service.TimeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@PersistJobDataAfterExecution
public class CampaignRenewalJob implements Job {

    private final BadgeService badgeService;

    private final CampaignRepository campaignRepository;

    private final TimeService timeService;

    private final NewsService newsService;


    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        String campaignIdStr = context.getTrigger().getJobDataMap().getString("campaignId");

        log.info("Renew campaign with id " + campaignIdStr);

        Campaign campaign = campaignRepository.findOne(Long.valueOf(campaignIdStr));


        if (campaign.isGenerateResults()) {
            newsService.prepareNews(campaign);
        }

        timeService.fitNextEndDate(campaign);
        campaignRepository.save(campaign);

        if (campaign.isRenewPeriod()) {
            Campaign nextCampaign = campaignRepository.save(campaign.setId(null));
            nextCampaign.getBadges().forEach(badge -> badgeService.save(badge.setCampaign(nextCampaign)));

            badgeService.rescheduleBadgeRenewal(nextCampaign);
        }

    }


}
