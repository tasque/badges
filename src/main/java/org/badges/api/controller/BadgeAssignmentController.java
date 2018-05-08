package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.EmployeeRepository;
import org.badges.service.NewsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequestMapping("/api/badges/assign")
@RequiredArgsConstructor
public class BadgeAssignmentController {

    private final BadgeRepository badgeRepository;

    private final EmployeeRepository employeeRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    private final NewsService newsService;

    @PostMapping
    @Transactional
    public News assignBadge(@RequestBody ImportBadgeAssignment importBadgeAssignment) {
        BadgeAssignment badgeAssignment = new BadgeAssignment();
        badgeAssignment.setComment(importBadgeAssignment.getComment());

        badgeAssignment.setBadge(badgeRepository.getOne(importBadgeAssignment.getBadgeId()));
        badgeAssignment.setToEmployees(new HashSet<>(
                employeeRepository.findAllById(importBadgeAssignment.getEmployeesIds())));
        badgeAssignmentRepository.save(badgeAssignment);


        return newsService.prepareNews(badgeAssignment);
    }
}
