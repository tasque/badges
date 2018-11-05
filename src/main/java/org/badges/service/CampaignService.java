package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.news.CampaignBadgeAssignmentNews;
import org.badges.db.NewsVisibility;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.CampaignRepository;
import org.badges.service.converter.BadgeConverter;
import org.badges.service.converter.UserConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CampaignService {


    private final CampaignRepository campaignRepository;

    private final UserConverter userConverter;

    private final BadgeConverter badgeConverter;

    @Transactional(readOnly = true)
    public Collection<CampaignBadgeAssignmentNews> news(Long id) {
        Campaign campaign = campaignRepository.getOne(id);

        if (campaign.isHiddenAlways()) {
            throw new EntityNotFoundException("Campaign is hidden");
        }
        if (campaign.isHiddenBeforeEnd() && campaign.getEndDate().after(new Date())) {
            throw new EntityNotFoundException("Campaign is not open yet");
        }

        return campaign.getBadgeAssignments().stream()
                .filter(ba -> ba.getNews().getNewsVisibility() == NewsVisibility.PUBLIC)
                .map(ba -> new CampaignBadgeAssignmentNews()
                        .setAssigner(userConverter.convertForNews(ba.getAssigner()))
                        .setBadge(badgeConverter.badgeNews(ba.getBadge()))
                        .setComment(ba.getComment())
                        .setDate(ba.getDate())
                        .setToUsers(ba.getToUsers().stream().map(userConverter::convertForNews).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
