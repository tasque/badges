package org.badges.db.repository;

import org.badges.db.BadgeAssignment;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BadgeAssignmentRepository extends JpaRepository<BadgeAssignment, Long> {

    List<BadgeAssignment> findAllByAssignerIdAndBadgeIdAndDateAfter(long assignerId, long badgeId, Date from);
}
