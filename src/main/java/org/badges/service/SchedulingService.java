package org.badges.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.campaign.Campaign;
import org.badges.job.CampaignRenewalJob;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulingService {

    private final Scheduler scheduler;

    @SneakyThrows
    public void rescheduleBadgeRenewal(Campaign campaign) {
        if (campaign == null) {
            return;
        }
        long campaignId = campaign.getId();
        String name = getCampaignNameJob(campaignId);
        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(name)
                .storeDurably()
                .ofType(CampaignRenewalJob.class)
                .build();

        if (scheduler.checkExists(jobDetail.getKey())) {
            log.warn("Found scheduled triggers for " + jobDetail);
            List<TriggerKey> triggers = scheduler.getTriggersOfJob(jobDetail.getKey()).stream()
                    .map(Trigger::getKey)
                    .collect(Collectors.toList());
            scheduler.unscheduleJobs(triggers);
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("campaignId", campaignId + "");

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getJobTrigger(name))
                .forJob(jobDetail)
                .startAt(campaign.getEndDate())
                .usingJobData(jobDataMap)
                .build();
        scheduler.scheduleJob(jobDetail, Collections.singleton(trigger), true);
    }

    private TriggerKey getJobTrigger(String name) {
        return new TriggerKey(name + "-Trigger");
    }

    @SneakyThrows
    public void checkCampaignJob(Campaign campaign) {
        if (campaign != null) {
            Trigger trigger = scheduler.getTrigger(getJobTrigger(getCampaignNameJob(campaign.getId())));
            if (trigger == null || trigger.getNextFireTime().before(new Date())) {
                rescheduleBadgeRenewal(campaign);
            }
        }
    }

    private String getCampaignNameJob(long campaignId) {
        return CampaignRenewalJob.class.getSimpleName() + "-" + campaignId;
    }
}
