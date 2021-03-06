package org.badges.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.CampaignRepository;
import org.badges.service.BadgeService;
import org.badges.service.NewsService;
import org.badges.service.SchedulingService;
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

    private final SchedulingService schedulingService;


    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        String campaignIdStr = context.getTrigger().getJobDataMap().getString("campaignId");

        log.info("Renew campaign with id " + campaignIdStr);

        Campaign campaign = campaignRepository.findOne(Long.valueOf(campaignIdStr));

        Campaign nextCampaign;
        if (campaign.isGenerateResults() && newsService.prepareNews(campaign) != null) {
            nextCampaign = campaignRepository.save(new Campaign()
                    .setCountPerCampaign(campaign.getCountPerCampaign())
                    .setCountToOneUser(campaign.getCountToOneUser())
                    .setToUsersMax(campaign.getToUsersMax())
                    .setGenerateResults(campaign.isGenerateResults())
                    .setHiddenAlways(campaign.isHiddenAlways())
                    .setHiddenBeforeEnd(campaign.isHiddenBeforeEnd())
                    .setRenewPeriod(campaign.isRenewPeriod())
                    .setDescription(campaign.getDescription())
                    .setImageUrl(campaign.getImageUrl())
                    .setPeriod(campaign.getPeriod())
                    .setStartDate(campaign.getStartDate())
                    .setEndDate(campaign.getEndDate())
            );
        } else {
            nextCampaign = campaign;// no reason to create new campaign
        }


        if (campaign.isRenewPeriod()) {
            timeService.fitNextEndDate(nextCampaign);

            campaign.getBadges().forEach(badge -> badgeService.save(badge.setCampaign(nextCampaign)));

            schedulingService.rescheduleBadgeRenewal(nextCampaign);
        }

    }


}
