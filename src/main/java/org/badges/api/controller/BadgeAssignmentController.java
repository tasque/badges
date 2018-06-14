package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.News;
import org.badges.service.BadgeAssignmentService;
import org.badges.service.converter.NewsConverter;
import org.badges.service.event.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/badges/assign")
@RequiredArgsConstructor
public class BadgeAssignmentController {

    private final BadgeAssignmentService badgeAssignmentService;

    private final NotificationService notificationService;

    private final NewsConverter newsConverter;

    @PostMapping
    public NewsDto assignBadge(@RequestBody ImportBadgeAssignment importBadgeAssignment) {
        News news = badgeAssignmentService.assignBadge(importBadgeAssignment);
        notificationService.notifyEmployees(news);

        return newsConverter.convert(news);
    }
}
