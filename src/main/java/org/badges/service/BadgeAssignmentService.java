package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.User;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.UserRepository;
import org.badges.security.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BadgeAssignmentService {

    private final BadgeRepository badgeRepository;

    private final UserRepository userRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    private final NewsService newsService;

    private final RequestContext requestContext;

    @Transactional
    public News assignBadge(ImportBadgeAssignment importBadgeAssignment) {
        Badge badge = badgeRepository.getOne(importBadgeAssignment.getBadgeId());

        validateCampaignAssignment(badge, importBadgeAssignment);

        BadgeAssignment badgeAssignment = new BadgeAssignment();
        badgeAssignment.setComment(importBadgeAssignment.getComment());

        badgeAssignment.setAssigner(requestContext.getCurrentUser());
        badgeAssignment.setBadge(badge);
        Optional.ofNullable(badge.getCampaign()).ifPresent(badgeAssignment::setCampaign);

        badgeAssignment.setToUsers(importBadgeAssignment.getUsersIds().stream()
                .map(userRepository::findOne)
                .collect(Collectors.toSet()));
        badgeAssignment.setDate(new Date());
        badgeAssignmentRepository.saveAndFlush(badgeAssignment);


        News news = newsService.prepareNews(badgeAssignment);
        badgeAssignment.setNews(news);
        return news;
    }

    private void validateCampaignAssignment(Badge badge, ImportBadgeAssignment importBadgeAssignment) {
        if (badge == null || !badge.isEnabled() || badge.isDeleted()) {
            throw new BadgeAssignmentValidationException("badge not available");
        }

        Campaign campaign = badge.getCampaign();
        if (campaign == null) {
            return;
        }
        if (campaign.outOfDate(new Date())) {
            throw new BadgeAssignmentValidationException("Campaign is out of date");
        }
        if (importBadgeAssignment.getUsersIds().size() > campaign.getToUsersMax()) {
            throw new BadgeAssignmentValidationException("Too many assignees");
        }

        List<BadgeAssignment> assigned = badgeAssignmentRepository.findAllByAssignerIdAndBadgeIdAndDateAfter(
                requestContext.getCurrentUserId(), importBadgeAssignment.getBadgeId(), campaign.getStartDate());

        if (assigned.size() >= campaign.getCountPerCampaign()) {
            throw new BadgeAssignmentValidationException("Too many assignments");
        }

        assigned.stream().flatMap(ba -> ba.getToUsers().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream().filter(countPerUser -> campaign.getCountToOneUser() >= countPerUser)
                .findAny()
                .ifPresent(v -> {
                    throw new BadgeAssignmentValidationException("Too many assigned");
                });

    }

    @Transactional(readOnly = true)
    public List<User> filterUsers(List<User> users, Long badgeId) {
        if (badgeId != null) {
            Badge badge = badgeRepository.findOne(badgeId);
            Campaign campaign = badge.getCampaign();
            if (badge.isEnabled() && campaign != null) {
                List<BadgeAssignment> assigned = badgeAssignmentRepository.findAllByAssignerIdAndBadgeIdAndDateAfter(
                        requestContext.getCurrentUserId(), badgeId, campaign.getStartDate());

                if (assigned.size() >= campaign.getCountPerCampaign()) {
                    return Collections.emptyList();
                }

                Map<User, Long> count = assigned.stream().flatMap(ba -> ba.getToUsers().stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                users.removeIf(user -> count.getOrDefault(user, 0L) >= campaign.getCountToOneUser());

            }
        }

        return users;
    }
}