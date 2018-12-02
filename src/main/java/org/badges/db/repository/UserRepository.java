package org.badges.db.repository;


import org.badges.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // ilike ~~*
    @Query(nativeQuery = true, value =
            "select * from user u " +
                    "where u.enabled = true " +
                    "   and (u.name ~~* :search or u.native_name ~~* :search) " +
                    "and u.id != :id " +
                    "limit :size")
    List<User> findUsers(@Param("search") String search, @Param("id") Long id, @Param("size") int size);
}
