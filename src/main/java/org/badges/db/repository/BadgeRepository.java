package org.badges.db.repository;


import org.badges.db.Badge;
import org.badges.db.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Page<Badge> findAllByDeletedFalseAndCompany(Pageable pageable, Company company);

    Badge getByDeletedFalseAndIdAndCompany(long id, Company company);
}
