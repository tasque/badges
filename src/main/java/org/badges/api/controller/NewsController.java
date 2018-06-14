package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.repository.NewsRepository;
import org.badges.service.converter.NewsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsRepository newsRepository;

    private final NewsConverter newsConverter;

    @GetMapping
    public Page<NewsDto> findNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsConverter::convert);
    }
}
