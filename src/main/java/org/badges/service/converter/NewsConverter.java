package org.badges.service.converter;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.News;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsConverter {

    private final EmployeeConverter employeeConverter;

    public NewsDto convert(News news) {
        return new NewsDto()
                .setComment(news.getComment())
                .setNewsType(news.getNewsType())
                .setEntityId(news.getEntityId())
                .setId(news.getId())
                .setToEmployees(news.getToEmployees().stream()
                        .map(employeeConverter::convert)
                        .collect(Collectors.toList()))
                .setAuthor(employeeConverter.convert(news.getAuthor()))
                .setTags(Collections.emptyList());
    }
}
