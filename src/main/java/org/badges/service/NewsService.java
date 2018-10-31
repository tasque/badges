package org.badges.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.NewsType;
import org.badges.db.NewsVisibility;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;

import static org.badges.db.NewsVisibility.PUBLIC;
import static org.badges.db.QNews.news;

@Component
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public Page<News> findNews(NewsQueryParams pageable) {
        BooleanExpression isPublic = news.newsVisibility.in(PUBLIC);

        BooleanExpression predicate = news.deleted.isFalse();
        if (pageable.getUserId() != 0) {
            predicate = predicate.andAnyOf(
                    news.author.id.eq(pageable.getUserId())
                            .and(news.newsVisibility.in(PUBLIC, NewsVisibility.PRIVATE)),
                    news.toUsers.any().id.eq(pageable.getUserId()).and(isPublic));
        } else {
            predicate = predicate.and(isPublic);
        }
        return newsRepository.findAll(predicate, pageable);
    }

    public News news(long id) {
        News news = newsRepository.getOne(id);
        if (news.isDeleted()) {
            throw new EntityNotFoundException("Not found news " + id);
        }
        return news;
    }

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
        Campaign campaign = badgeAssignment.getBadge().getCampaign();
        if (campaign != null)
        {
            if (campaign.isHiddenBeforeEnd() || campaign.isHiddenAlways()) {
                news.setNewsVisibility(NewsVisibility.PRIVATE);
            }
        }
    }
}
