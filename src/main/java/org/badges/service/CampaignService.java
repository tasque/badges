package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.news.AchtungNewsDto;
import org.badges.api.domain.news.ActionRequiredType;
import org.badges.api.domain.news.CampaignBadgeAssignmentNews;
import org.badges.api.domain.news.UserNewsDto;
import org.badges.db.Badge;
import org.badges.db.NewsVisibility;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
        Date now = new Date();
        if (campaign.isHiddenBeforeEnd() && campaign.getEndDate().after(now)) {
            throw new EntityNotFoundException("Campaign is not open yet");
        }

        if (campaign.outOfDate(now)) {
            userNewsViewRepository.saveQuietly(requestContext.getCurrentUserId(), campaign.getId(), UserViewEventType.OPEN_CAMPAIGN_RESULTS.name());
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

    @Transactional(readOnly = true)
    public List<AchtungNewsDto> getRecentCampaigns(List<Badge> catalogue) {
        Date now = new Date();
        Long currentUserId = requestContext.getCurrentUserId();


        Collection<Campaign> campaigns = catalogue.stream()
                .map(Badge::getCampaign)
                .filter(Objects::nonNull)
                .filter(campaign -> !campaign.getBadgeAssignments().isEmpty())
                .collect(Collectors.toSet());

        List<Campaign> recentCampaigns = campaignRepository.findRecentCampaigns(currentUserId, now);
        recentCampaigns.stream()
                .filter(c -> !c.isHiddenAlways())
                .filter(c -> !c.getBadgeAssignments().isEmpty())
                .forEach(campaigns::add);

        List<Campaign> activeCampaigns = campaignRepository.findActiveCampaigns(currentUserId, now);
        activeCampaigns.stream()
                .filter(c -> !c.isHiddenBeforeEnd())
                .filter(c -> !c.getBadgeAssignments().isEmpty())
                .forEach(campaigns::add);


        return campaigns.stream()
                .map(c -> {
                    List<UserNewsDto> users = Collections.emptyList();
                    if (!c.isHiddenAlways()) {
                        users = c.getBadgeAssignments().stream().flatMap(ba -> ba.getToUsers().stream().distinct()).map(userConverter::convertForNews).collect(Collectors.toList());
                    }
                    return new AchtungNewsDto().setEntityId(c.getId())
                            .setComment(c.getDescription())
                            .setActionRequired(ActionRequiredType.LOOK_AT_CAMPAIGN_RESULTS)
                            .setImageUrl(c.getImageUrl())
                            .setToUsers(users);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AchtungNewsDto> getActiveCampaigns() {
        List<Campaign> activeCampaigns = campaignRepository.findActiveCampaigns(new Date());

        return activeCampaigns.stream()
                .map(c -> {
                    List<UserNewsDto> users = Collections.emptyList();
                    if (!c.isHiddenAlways() && !c.isHiddenBeforeEnd()) {
                        users = c.getBadgeAssignments().stream().flatMap(ba -> ba.getToUsers().stream().distinct()).map(userConverter::convertForNews).collect(Collectors.toList());
                    }
                    return new AchtungNewsDto().setEntityId(c.getId())
                            .setComment(c.getDescription())
                            .setActionRequired(ActionRequiredType.LOOK_AT_CAMPAIGN_RESULTS)
                            .setImageUrl(c.getImageUrl())
                            .setToUsers(users);
                })
                .collect(Collectors.toList());
    }
}
