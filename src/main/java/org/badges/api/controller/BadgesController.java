package org.badges.api.controller;


import lombok.RequiredArgsConstructor;
import org.badges.api.domain.admin.AdminBadge;
import org.badges.db.Badge;
import org.badges.db.repository.BadgeRepository;
import org.badges.service.converter.BadgeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController("/api/badges")
@RequiredArgsConstructor
public class BadgesController {

    private final BadgeRepository badgeRepository;

    private final BadgeConverter badgeConverter;

    @GetMapping
    public Page<AdminBadge> getBadges(Pageable pageable) {
        return badgeRepository.findAllByDeletedFalse(pageable)
                .map(badgeConverter::convert);
    }

    @GetMapping("/{id}")
    public AdminBadge getBadge(@PathVariable("id") long id) {
        return Optional.ofNullable(badgeRepository.getByDeletedFalseAndId(id))
                .map(badgeConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find badge with id " + id));
    }

    @PostMapping
    public AdminBadge save(AdminBadge badge) {
        Badge saved = badgeRepository.save(badgeConverter.convert(badge));
        return badgeConverter.convert(saved);
    }

    @DeleteMapping
    public void delete(long id) {
        Optional.ofNullable(badgeRepository.getByDeletedFalseAndId(id))
                .map(badge -> badgeRepository.save(badge.setDeleted(true)))
                .orElseThrow(() -> new EntityNotFoundException("Cannot find badge with id " + id));
    }
}