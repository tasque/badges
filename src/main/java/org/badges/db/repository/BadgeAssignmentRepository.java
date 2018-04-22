package org.badges.db.repository;

import org.badges.db.BadgeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeAssignmentRepository extends JpaRepository<BadgeAssignment, Long> {
}
