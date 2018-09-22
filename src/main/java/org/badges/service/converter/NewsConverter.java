package org.badges.service.converter;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.News;
import org.badges.db.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsConverter {

    private static final int maxSize = 8;

    private final UserConverter userConverter;

    private final BadgeConverter badgeConverter;


    public NewsDto convert(News news) {
        return new NewsDto()
                .setComment(getShortComment(news))
                .setNewsType(news.getNewsType())
                .setEntityId(news.getEntityId())
                .setId(news.getId())
                .setTotalToUsers(news.getToUsers().size())
                .setToUsers(getToUsers(news).stream()
                        .map(userConverter::convertNews)
                        .collect(Collectors.toList()))
                .setAuthor(userConverter.convertNews(news.getAuthor()))
                .setTags(Collections.emptyList())
                .setDate(news.getCreateDate())
                .setReason(badgeConverter.badgeNews(news));
    }

    private String getShortComment(News news) {
        return news.getComment().substring(0, Math.min(500, news.getComment().length()));
    }

    private Collection<User> getToUsers(News news) {
        Set<User> toUsers = news.getToUsers();
        if (toUsers.size() > maxSize) {
            List<User> shortList = new ArrayList<>(toUsers);
            Collections.shuffle(shortList);
            return shortList.subList(0, maxSize);
        }
        return toUsers;
    }
}
