package org.badges.db.repository;


import org.badges.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value =
            "select u.* from public.user u " +
                    "where u.enabled = true " +
                    "   and (u.name ilike %:search% or u.native_name ilike %:search%) " +
                    "and u.id != :id " +
                    "limit :size")
    List<User> findUsers(@Param("search") String search, @Param("id") Long id, @Param("size") int size);

    User findByEmail(String email);

    @Query(nativeQuery = true, value =
            "select u.* from public.user u " +
                    "inner join user_badge_assignment uba on u.id = uba.user_id " +
                    "inner join badge_assignment ba on uba.badge_assignment_id = ba.id " +
                    "where u.date_of_birth = date (now()) " +
                    "   and u.id != :userId " +
                    "   and date(ba.date) = date (now()) " +
                    "   and ba.badge_id = :badgeId " +
                    "   and ba.assigner_id = :userId ")
    List<User> findUsersWithoutBadge(@Param("badgeId") Long badgeId, @Param("userId") Long userId);
}
