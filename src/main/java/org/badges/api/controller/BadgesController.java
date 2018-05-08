package org.badges.api.controller;


import lombok.RequiredArgsConstructor;
import org.badges.db.Badge;
import org.badges.db.repository.BadgeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/badges")
@RequiredArgsConstructor
public class BadgesController {

    private final BadgeRepository badgeRepository;

    @GetMapping
    public Page<Badge> getBadges(Pageable pageable) {
        return badgeRepository.findAllByDeletedFalse(pageable);
    }

    @GetMapping("/{id}")
    public Badge getBadge(@PathVariable("id") long id) {
        return badgeRepository.getByDeletedFalseAndAndId(id);
    }

    @PostMapping
    public Badge save(Badge badge) {
        return badgeRepository.save(badge);
    }

    @DeleteMapping
    public void delete(long id) {
        Badge badge = badgeRepository.getOne(id);
        badge.setDeleted(true);
        badgeRepository.save(badge);
    }
}