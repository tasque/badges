package org.badges.db.repository;


import org.badges.db.campaign.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query(nativeQuery = true, value = "select distinct c.* from public.campaign c " +
            "inner join badge_assignment ba on c.id = ba.campaign_id " +
            "where ba.assigner_id = :userId and c.end_date < :date " +
            "   and not exists (select 1 from user_news_views unw " +
            "                   where unw.entity_id = c.id and unw.user_id = :userId " +
            "                       and unw.event_type = 'OPEN_CAMPAIGN_RESULTS')")
    List<Campaign> findRecentCampaigns(@Param("userId") Long userId, @Param("date") Date date);


    @Query(nativeQuery = true, value = "select distinct c.* from public.campaign c " +
            "inner join badge_assignment ba on c.id = ba.campaign_id " +
            "where ba.assigner_id = :userId and c.end_date > :date and c.start_date < :date")
    List<Campaign> findActiveCampaigns(@Param("userId") Long userId, @Param("date") Date date);

    @Query(nativeQuery = true, value = "select distinct c.* from public.campaign c " +
            "where c.start_date< :date and c.end_date > :date")
    List<Campaign> findActiveCampaigns(@Param("date") Date date);
}
