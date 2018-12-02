package org.badges.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;



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



    public Badge save(Badge badge) {
        return badgeRepository.save(badge);
    }

}
