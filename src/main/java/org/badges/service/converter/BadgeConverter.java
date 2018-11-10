package org.badges.service.converter;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.admin.AdminBadge;
import org.badges.api.domain.catalog.CatalogBadge;
import org.badges.api.domain.news.BadgeNewsDto;
import org.badges.db.Badge;
import org.badges.db.News;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeConverter {

    private final BadgeAssignmentRepository badgeAssignmentRepository;

    public AdminBadge convert(Badge badge) {
        return new AdminBadge()
                .setId(badge.getId())
                .setEnabled(badge.isEnabled())
                .setName(badge.getName())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl());
    }


    public Badge convert(AdminBadge badge) {
        return new Badge()
                .setId(badge.getId())
                .setEnabled(badge.isEnabled())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl())
                .setName(badge.getName());
    }

    public CatalogBadge catalogBadge(Badge badge, Long currentUserId) {
        return new CatalogBadge()
                .setId(badge.getId())
                .setCategory(badge.getCategory())
                .setName(badge.getName())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl())
                .setCountLeft(getCountLeft(badge, currentUserId))
                .setSpecial(badge.getCampaign() != null);
    }

    private Integer getCountLeft(Badge badge, Long currentUserId) {
        Campaign campaign = badge.getCampaign();
        if (campaign == null) {
            return null;
        }
        int elapsed = badgeAssignmentRepository.findAllByAssignerIdAndBadgeIdAndDateAfter(currentUserId, badge.getId(), campaign.getStartDate())
                .size();
        return campaign.getCountPerCampaign() - elapsed;
    }

    public BadgeNewsDto badgeNews(News news) {
        return new BadgeNewsDto()
                .setId(Long.valueOf(news.getArg0()))
                .setName(news.getArg1())
                .setImageUrl(news.getArg2());
    }


    public BadgeNewsDto badgeNews(Badge badge) {
        return new BadgeNewsDto()
                .setId(badge.getId())
                .setName(badge.getName())
                .setImageUrl(badge.getImageUrl());
    }


}
