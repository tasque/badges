package org.badges.api.controller;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.QNews;
import org.badges.db.repository.NewsRepository;
import org.badges.service.converter.NewsConverter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    private final NewsRepository newsRepository;

    private final NewsConverter newsConverter;

    @GetMapping
    public Page<NewsDto> showNews(NewsQueryParams pageable) {
        BooleanExpression predicate = QNews.news.deleted.isFalse();
        if (pageable.getUserId() != 0) {
            predicate = predicate.andAnyOf(
                    QNews.news.author.id.eq(pageable.getUserId()),
                    QNews.news.toUsers.any().id.eq(pageable.getUserId()));
        }
        return newsRepository.findAll(predicate, pageable)
                .map(newsConverter::convert);
    }
}
