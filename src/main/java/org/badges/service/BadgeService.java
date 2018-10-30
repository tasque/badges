package org.badges.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.job.CampaignRenewalJob;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    private final Scheduler scheduler;


    @Transactional(readOnly = true)
    public List<Badge> badgesForCatalogue(Long userId) {
        List<Badge> availableBadges = badgeRepository.findAllByEnabledTrueAndDeletedFalse();
        Date now = new Date();

        availableBadges.removeIf(b -> meetsCampaignLimitation(b, userId, now));

        return availableBadges;
    }

    private boolean meetsCampaignLimitation(Badge badge, Long userId, Date now) {
        Campaign campaign = badge.getCampaign();
        if (campaign == null) {
            return false;
        }
        if (campaign.outOfDate(now)) {
            log.debug("Campaign {} out of date", campaign);
            return true;
        }
        List<BadgeAssignment> assignments = badgeAssignmentRepository.findAllByAssignerIdAndBadgeIdAndDateAfter(userId, badge.getId(), campaign.getStartDate());

        log.debug("Already assigned {} of {} badges", assignments.size(), campaign.getCountPerCampaign());
        return assignments.size() >= campaign.getCountPerCampaign();
    }

    @Transactional(readOnly = true)
    public Badge getSpecialBadge(long id) {
        Badge badge = badgeRepository.findOne(id);

        Campaign campaign = badge.getCampaign();
        if (badge.isDeleted() || !badge.isEnabled() || campaign == null) {
            throw new RuntimeException("there is no special badge " + id);
        }

        return badge;
    }


    @SneakyThrows
    public void rescheduleBadgeRenewal(Campaign campaign) {
        if (campaign == null) {
            return;
        }
        long campaignId = campaign.getId();
        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(CampaignRenewalJob.class.getSimpleName() + "-" + campaignId)
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
                .forJob(jobDetail)
                .startAt(campaign.getEndDate())
                .usingJobData(jobDataMap)
                .build();
        scheduler.scheduleJob(jobDetail, Collections.singleton(trigger), true);
    }

    public Badge save(Badge badge) {
        return badgeRepository.save(badge);
    }

}
