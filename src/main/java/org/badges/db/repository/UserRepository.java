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
}
