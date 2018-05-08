package org.badges.service;

import lombok.RequiredArgsConstructor;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.NewsType;
import org.badges.db.repository.NewsRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News prepareNews(BadgeAssignment badgeAssignment) {
        News news = new News();

        news.setAuthor(badgeAssignment.getAssigner());
        news.setComment(badgeAssignment.getComment());
        news.setNewsType(NewsType.BADGE_ASSIGNMENT);
        news.setTags(badgeAssignment.getTags());
        news.setToEmployees(badgeAssignment.getToEmployees());
        news.setCompany(badgeAssignment.getAssigner().getCompany());

        return newsRepository.save(news);
    }
}
