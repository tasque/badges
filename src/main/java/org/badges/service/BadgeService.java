package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.campaign.BadgeCampaignRule;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    @Transactional(readOnly = true)
    public List<Badge> badgesForCatalogue(Long userId) {
        List<Badge> availableBadges = badgeRepository.findAllByEnabledTrueAndDeletedFalse();

        availableBadges.removeIf(b -> campaignLimitation(b, userId));

        return availableBadges;
    }

    private boolean campaignLimitation(Badge badge, Long userId) {
        BadgeCampaignRule rule = badge.getBadgeCampaignRule();
        if (rule == null) {
            return false;
        }
        List<BadgeAssignment> assignments = badgeAssignmentRepository.findAllByAssignerIdAndBadgeIdAndDateAfter(userId, badge.getId(), rule.getStartDate());

        return assignments.size() >= rule.getCountPerCampaign();
    }
}
