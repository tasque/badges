package org.badges.api.controller;


import lombok.RequiredArgsConstructor;
import org.badges.db.Badge;
import org.badges.db.repository.BadgeRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController("/api/badges")
@RequiredArgsConstructor
public class BadgesController {

    private final BadgeRepository badgeRepository;

    @GetMapping
    public List<Badge> getBadges() {
        return badgeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Badge getBadge(@PathVariable("id") long id) {
        return badgeRepository.getOne(id);
    }

    @PostMapping
    public Badge save(Badge badge) {
        return badgeRepository.save(badge);
    }

    @DeleteMapping
    public void delete(long id) {
        badgeRepository.deleteById(id);
    }
}