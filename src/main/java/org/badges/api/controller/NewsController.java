package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.api.domain.news.NewsDto;
import org.badges.service.NewsService;
import org.badges.service.converter.NewsConverter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
@Slf4j
public class NewsController {


    private final NewsService newsService;

    private final NewsConverter newsConverter;

    @GetMapping
    public Page<NewsDto> showNews(NewsQueryParams pageable) {

        return newsService.findNews(pageable)
                .map(newsConverter::shortConvert);
    }

    @GetMapping("/{id}")
    public NewsDto getById(@PathVariable("id") long id) {

        return newsConverter.convert(
                newsService.news(id));
    }
}
