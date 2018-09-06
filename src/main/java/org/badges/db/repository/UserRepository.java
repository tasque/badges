package org.badges.db.repository;


import org.badges.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCaseAndEnabledIsTrueAndIdIsNot(String name, Long id);
}
