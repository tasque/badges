package org.badges.api.controller;


import lombok.RequiredArgsConstructor;
import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.api.domain.catalog.CatalogBadge;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.Badge;
import org.badges.db.News;
import org.badges.security.RequestContext;
import org.badges.service.BadgeAssignmentService;
import org.badges.service.BadgeService;
import org.badges.service.converter.BadgeConverter;
import org.badges.service.converter.NewsConverter;
import org.badges.service.event.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgesController {

    private final BadgeAssignmentService badgeAssignmentService;

    private final NotificationService notificationService;

    private final NewsConverter newsConverter;

    private final BadgeService badgeService;

    private final BadgeConverter badgeConverter;

    private final RequestContext requestContext;

    @GetMapping("/catalog")
    public Map<String, List<CatalogBadge>> catalog() {
        Map<String, List<CatalogBadge>> catalogue = new LinkedHashMap<>();
        Long currentUserId = requestContext.getCurrentUserId();
        List<Badge> badges = badgeService.badgesForCatalogue(currentUserId);

        // campaign goes first
        badges.sort(Comparator.comparing(badge -> badge.getCampaign() != null ? 0 : 1));

        badges.forEach(badge ->
                catalogue.computeIfAbsent(badge.getCategory(), key -> new ArrayList<>())
                        .add(badgeConverter.catalogBadge(badge, currentUserId)));

        return catalogue;
    }


    @PostMapping("/assign")
    public NewsDto assignBadge(@RequestBody ImportBadgeAssignment importBadgeAssignment) {
        News news = badgeAssignmentService.assignBadge(importBadgeAssignment);
        notificationService.notifyUsers(news);

        return newsConverter.shortConvert(news);
    }
}