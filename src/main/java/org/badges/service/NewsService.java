package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.NewsType;
import org.badges.db.repository.NewsRepository;
import org.badges.security.TenantContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final TenantContext tenantContext;

    public News prepareNews(BadgeAssignment badgeAssignment) {
        News news = new News();


        news.setAuthor(badgeAssignment.getAssigner());
        news.setComment(badgeAssignment.getComment());
        news.setNewsType(NewsType.BADGE_ASSIGNMENT);
        news.setEntityId(badgeAssignment.getId());
//        news.setEntity(badgeAssignment);
        news.setTags(badgeAssignment.getTags());
        news.setToEmployees(new HashSet<>(badgeAssignment.getToEmployees()));
        news.setCompany(tenantContext.getCurrentCompany());

        return newsRepository.save(news);
    }
}
