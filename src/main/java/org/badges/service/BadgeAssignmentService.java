package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.User;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.UserRepository;
import org.badges.security.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
    public News assignBadge(@RequestBody ImportBadgeAssignment importBadgeAssignment) {

        BadgeAssignment badgeAssignment = new BadgeAssignment();
        badgeAssignment.setComment(importBadgeAssignment.getComment());

        badgeAssignment.setAssigner(userRepository.getOne(importBadgeAssignment.getAssignerId()));
        badgeAssignment.setBadge(badgeRepository.getOne(importBadgeAssignment.getBadgeId()));
        badgeAssignment.setToUsers(importBadgeAssignment.getUsersIds().stream()
                .map(userRepository::findOne)
                .collect(Collectors.toSet()));
        badgeAssignmentRepository.save(badgeAssignment);


        News news = newsService.prepareNews(badgeAssignment);
        badgeAssignment.setNews(news);
        return news;

    }
}