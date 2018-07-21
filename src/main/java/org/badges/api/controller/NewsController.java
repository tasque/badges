package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.repository.NewsRepository;
import org.badges.service.converter.NewsConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsRepository newsRepository;

    private final NewsConverter newsConverter;

    @GetMapping
    public List<NewsDto> findNews(NewsQueryParams pageable) {
        return newsRepository.findAll().stream()
                .map(newsConverter::convert)
                .collect(Collectors.toList());
    }
}
