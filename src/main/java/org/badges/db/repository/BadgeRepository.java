package org.badges.db.repository;


import org.badges.db.Badge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Page<Badge> findAllByDeletedFalse(Pageable pageable);

    List<Badge> findAllByEnabledTrueAndDeletedFalse();

    Badge getByDeletedFalseAndId(long id);
}
