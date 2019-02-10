package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.news.AchtungNewsDto;
import org.badges.api.domain.news.ActionRequiredType;
import org.badges.api.domain.news.CampaignBadgeAssignmentNews;
import org.badges.db.NewsVisibility;
import org.badges.db.UserNewsView;
import org.badges.db.UserViewEventType;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.CampaignRepository;
import org.badges.db.repository.UserNewsViewRepository;
import org.badges.security.RequestContext;
import org.badges.service.converter.BadgeConverter;
import org.badges.service.converter.UserConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CampaignService {

    private final UserNewsViewRepository userNewsViewRepository;

    private final CampaignRepository campaignRepository;

    private final UserConverter userConverter;

    private final RequestContext requestContext;

    private final BadgeConverter badgeConverter;

    @Transactional
    public Collection<CampaignBadgeAssignmentNews> news(Long id) {
        Campaign campaign = campaignRepository.getOne(id);

        if (campaign.isHiddenAlways()) {
            throw new EntityNotFoundException("Campaign is hidden");
        }
        if (campaign.isHiddenBeforeEnd() && campaign.getEndDate().after(new Date())) {
            throw new EntityNotFoundException("Campaign is not open yet");
        }

        userNewsViewRepository.saveQuietly(requestContext.getCurrentUserId(), campaign.getId(), UserViewEventType.OPEN_CAMPAIGN_RESULTS);

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

    @Transactional(readOnly = true)
    public List<AchtungNewsDto> getRecentCampaigns() {
        Set<Long> viewedCampaigns = userNewsViewRepository.findAllByUserIdAndEventType(requestContext.getCurrentUserId(), UserViewEventType.OPEN_CAMPAIGN_RESULTS)
                .stream().map(UserNewsView::getEntityId)
                .collect(Collectors.toSet());
        List<Campaign> participatedCampaigns = campaignRepository.findRecentCampaigns(requestContext.getCurrentUserId());

        return participatedCampaigns.stream().filter(c -> viewedCampaigns.contains(c.getId()))
                .map(c -> new AchtungNewsDto().setEntityId(c.getId())
                        .setComment(c.getDescription())
                        .setActionRequired(ActionRequiredType.LOOK_AT_CAMPAIGN_RESULTS)
                        .setImageUrl(c.getImageUrl())
                        .setToUsers(c.getBadgeAssignments().stream().flatMap(ba -> ba.getToUsers().stream().distinct()).map(userConverter::convertForNews).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
