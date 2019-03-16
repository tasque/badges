package org.badges.db.repository;

import org.badges.db.UserNewsViews;
import org.badges.db.UserViewEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNewsViewRepository extends JpaRepository<UserNewsViews, Long> {


    @Modifying
    @Query(value = "INSERT INTO public.user_news_views (user_id, entity_id, event_type) " +
            "VALUES (:userId, :entityId, :eventType) " +
            "ON CONFLICT (unique_unw_user_entity_event) DO update set count_of_views = count_of_views + 1", nativeQuery = true)
    void saveQuietly(@Param("userId") Long userId, @Param("entityId") Long entityId, @Param("eventType") String eventType);

    List<UserNewsViews> findAllByUserIdAndEventType(Long userId, UserViewEventType eventType);
}
