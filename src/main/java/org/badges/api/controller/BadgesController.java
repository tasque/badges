package org.badges.api.controller;


import lombok.RequiredArgsConstructor;
import org.badges.api.domain.admin.AdminBadge;
import org.badges.api.domain.catalog.CatalogBadge;
import org.badges.db.Badge;
import org.badges.db.UserPermission;
import org.badges.db.repository.BadgeRepository;
import org.badges.security.annotation.RequiredPermission;
import org.badges.service.converter.BadgeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgesController {

    private final BadgeRepository badgeRepository;

    private final BadgeConverter badgeConverter;

    @GetMapping("/catalog")
    public List<CatalogBadge> catalog() {
        return badgeRepository.findAllByEnabledTrueAndDeletedFalse().stream()
                .map(badgeConverter::catalogBadge)
                .collect(Collectors.toList());
    }

    @RequiredPermission(UserPermission.READ_BADGE)
    @GetMapping
    public Page<AdminBadge> getBadges(Pageable pageable) {
        return badgeRepository.findAllByDeletedFalse(pageable)
                .map(badgeConverter::convert);
    }

    @RequiredPermission(UserPermission.READ_BADGE)
    @GetMapping("/{id}")
    public AdminBadge getBadge(@PathVariable("id") long id) {
        return Optional.ofNullable(badgeRepository.getByDeletedFalseAndId(id))
                .map(badgeConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find badge with id " + id));
    }

    @RequiredPermission(UserPermission.UPDATE_BADGE)
    @PostMapping
    public AdminBadge save(AdminBadge badge) {
        Badge saved = badgeRepository.save(badgeConverter.convert(badge));
        return badgeConverter.convert(saved);
    }

    @RequiredPermission(UserPermission.DELETE_BADGE)
    @DeleteMapping
    public void delete(long id) {
        Optional.ofNullable(badgeRepository.getByDeletedFalseAndId(id))
                .map(badge -> badgeRepository.save(badge.setDeleted(true)))
                .orElseThrow(() -> new EntityNotFoundException("Cannot find badge with id " + id));
    }
}