package org.badges.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.NewsType;
import org.badges.db.NewsVisibility;
import org.badges.db.campaign.Campaign;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.badges.db.NewsVisibility.PUBLIC;
import static org.badges.db.QNews.news;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;

    private final BadgeAssignmentRepository badgeAssignmentRepository;

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
        news.setTags(badgeAssignment.getTags());
        news.setToUsers(new HashSet<>(badgeAssignment.getToUsers()));
        news.setCreateDate(badgeAssignment.getDate());

        Badge badge = badgeAssignment.getBadge();
        news.setArg0(badge.getId().toString());
        news.setArg1(badge.getName());
        news.setArg2(badge.getImageUrl());

        return newsRepository.save(news);
    }

    public News prepareNews(Campaign campaign) {
        List<BadgeAssignment> assignments = campaign.getBadges().stream()
                .flatMap(b -> badgeAssignmentRepository.findAllByBadgeIdAndDateAfterAndDateBefore(
                        b.getId(),
                        campaign.getStartDate(),
                        campaign.getEndDate()).stream())
                .collect(Collectors.toList());

        if (assignments.isEmpty()) {
            log.info("No assignments found for {}", campaign);
            return null;
        }
        if (!campaign.isHiddenAlways()) {
            assignments.forEach(ba -> newsRepository.save(ba.getNews().setNewsVisibility(PUBLIC)));
        }


        News news = new News();
        news.setComment(campaign.getDescription());
        news.setNewsType(NewsType.CAMPAIGN_RESULTS);
        news.setNewsVisibility(PUBLIC);

        news.setEntityId(campaign.getId());
        news.setToUsers(assignments.stream().flatMap(ba -> ba.getToUsers().stream()).collect(Collectors.toSet()));
        news.setCreateDate(new Date());

        news.setArg0(campaign.getId() + "");
        news.setArg1(campaign.getDescription());
        news.setArg2(campaign.getImageUrl());

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
