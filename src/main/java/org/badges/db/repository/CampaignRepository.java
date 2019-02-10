package org.badges.db.repository;


import org.badges.db.campaign.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query(nativeQuery = true, value = "select distinct c.* from public.campaign c " +
            "inner join badge_assignment ba on c.id = ba.campaign_id " +
            "where ba.assigner_id = :userId and c.end_date < now()")
    List<Campaign> findRecentCampaigns(@Param("userId") Long userId);
}
