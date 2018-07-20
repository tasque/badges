package org.badges.service.converter;

import org.badges.api.domain.news.UserNewsDto;
import org.badges.db.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserNewsDto convert(User user) {
        return new UserNewsDto()
                .setId(user.getId())
                .setImageUrl(user.getImageUrl())
                .setName(user.getName());
    }
}
