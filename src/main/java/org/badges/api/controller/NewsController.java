package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.db.News;
import org.badges.db.repository.NewsRepository;
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

    @GetMapping
    public Page<News> findNews(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }
}
