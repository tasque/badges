package org.badges.db.repository;

import org.badges.db.UserNewsView;
import org.badges.db.UserViewEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNewsViewRepository extends JpaRepository<UserNewsView, Long> {


    @Modifying
    @Query(value = "INSERT INTO user_news_views (user_id, entity_id, event_type) " +
            "VALUES (:userId, :entityId), :eventType) ON CONFLICT DO NOTHING", nativeQuery = true)
    void saveQuietly(@Param("userId") Long userId, @Param("entityId") Long entityId, @Param("eventType") UserViewEventType eventType);

    List<UserNewsView> findAllByUserIdAndEventType(Long userId, UserViewEventType eventType);
}
