package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.NewsType;
import org.badges.db.NewsVisibility;
import org.badges.db.campaign.BadgeCampaignRule;
import org.badges.db.repository.NewsRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News prepareNews(BadgeAssignment badgeAssignment) {
        News news = new News();


        news.setAuthor(badgeAssignment.getAssigner());
        news.setComment(badgeAssignment.getComment());
        news.setNewsType(NewsType.BADGE_ASSIGNMENT);
        defineNewsVisibility(badgeAssignment, news);

        news.setEntityId(badgeAssignment.getId());
//        news.setEntity(badgeAssignment);
        news.setTags(badgeAssignment.getTags());
        news.setToUsers(new HashSet<>(badgeAssignment.getToUsers()));
        news.setCreateDate(badgeAssignment.getDate());

        Badge badge = badgeAssignment.getBadge();
        news.setArg0(badge.getId().toString());
        news.setArg1(badge.getName());
        news.setArg2(badge.getImageUrl());

        return newsRepository.save(news);
    }

    private void defineNewsVisibility(BadgeAssignment badgeAssignment, News news) {
        news.setNewsVisibility(NewsVisibility.PUBLIC);
        BadgeCampaignRule badgeCampaignRule = badgeAssignment.getBadge().getBadgeCampaignRule();
        if (badgeCampaignRule != null)
        {
            if (badgeCampaignRule.isHiddenBeforeEnd() || badgeCampaignRule.isHiddenBeforeEnd()) {
                news.setNewsVisibility(NewsVisibility.PRIVATE);
            }
        }
    }
}
