package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.EmployeeRepository;
import org.badges.security.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class BadgeAssignmentService {

    private final BadgeRepository badgeRepository;

    private final EmployeeRepository employeeRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    private final NewsService newsService;

    private final RequestContext requestContext;

    public News assignBadge(@RequestBody ImportBadgeAssignment importBadgeAssignment) {

        BadgeAssignment badgeAssignment = new BadgeAssignment();
        badgeAssignment.setComment(importBadgeAssignment.getComment());

        badgeAssignment.setAssigner(employeeRepository.getOne(importBadgeAssignment.getAssignerId()));
        badgeAssignment.setBadge(badgeRepository.getOne(importBadgeAssignment.getBadgeId()));
        badgeAssignment.setToEmployees(new HashSet<>(
                employeeRepository.findAllById(importBadgeAssignment.getEmployeesIds())));
        badgeAssignment.setCompany(requestContext.getCurrentTenant());
        badgeAssignmentRepository.save(badgeAssignment);


        News news = newsService.prepareNews(badgeAssignment);
        badgeAssignment.setNews(news);
        return news;

    }
}