package org.badges.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.campaign.BadgeCampaignRule;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.job.BadgeRenewalJob;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    private final Scheduler scheduler;

    private final TimeService timeService;


    @Transactional(readOnly = true)
    public List<Badge> badgesForCatalogue(Long userId) {
        List<Badge> availableBadges = badgeRepository.findAllByEnabledTrueAndDeletedFalse();

        availableBadges.removeIf(b -> meetsCampaignLimitation(b, userId));

        return availableBadges;
    }

    private boolean meetsCampaignLimitation(Badge badge, Long userId) {
        BadgeCampaignRule rule = badge.getBadgeCampaignRule();
        if (rule == null) {
            return false;
        }
        List<BadgeAssignment> assignments = badgeAssignmentRepository.findAllByAssignerIdAndBadgeIdAndDateAfter(userId, badge.getId(), rule.getStartDate());

        return assignments.size() >= rule.getCountPerCampaign();
    }

    @Transactional(readOnly = true)
    public Badge getSpecialBadge(long id) {
        Badge badge = badgeRepository.findOne(id);

        BadgeCampaignRule badgeCampaignRule = badge.getBadgeCampaignRule();
        if (badge.isDeleted() || !badge.isEnabled() || badgeCampaignRule == null) {
            throw new RuntimeException("there is no special badge " + id);
        }

        return badge;
    }


    @SneakyThrows
    public void rescheduleBadgeRenewal(BadgeCampaignRule badgeCampaignRule) {
        if (badgeCampaignRule == null) {
            return;
        }
        timeService.fitNextEndDate(badgeCampaignRule);
        Long badgeId = badgeCampaignRule.getBadge().getId();
        JobDetail jobDetail = JobBuilder.newJob()
                .storeDurably()
                .withIdentity(BadgeRenewalJob.class.getSimpleName() + "-" + badgeId)
                .ofType(BadgeRenewalJob.class)
                .build();

        if (scheduler.checkExists(jobDetail.getKey())) {
            log.warn("Found scheduled triggers for " + jobDetail);
            List<TriggerKey> collect = scheduler.getTriggersOfJob(jobDetail.getKey()).stream()
                    .map(Trigger::getKey)
                    .collect(Collectors.toList());
            scheduler.unscheduleJobs(collect);
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("badgeId", badgeId.toString());

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(badgeCampaignRule.getEndDate())
                .usingJobData(jobDataMap)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
