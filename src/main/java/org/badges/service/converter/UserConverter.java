package org.badges.service.converter;

import org.badges.api.domain.CurrentUser;
import org.badges.api.domain.UserDto;
import org.badges.api.domain.news.UserNewsDto;
import org.badges.db.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserNewsDto convertNews(User user) {
        return new UserNewsDto()
                .setId(user.getId())
                .setImageUrl(user.getImageUrl())
                .setName(user.getName());
    }

    public CurrentUser currentUser(User user) {
        return new CurrentUser()
                .setEmail(user.getEmail())
                .setId(user.getId())
                .setImageUrl(user.getImageUrl())
                .setName(user.getName())
                .setTitle(user.getTitle())
                .setUserPermissions(user.getUserRoles().stream()
                        .flatMap(userRole -> userRole.getUserPermissions().stream())
                        .distinct()
                        .collect(Collectors.toList()));
    }


    public UserDto convertUser(User user) {
        return new UserDto()
                .setId(user.getId())
                .setImageUrl(user.getImageUrl())
                .setName(user.getName())
                .setTitle(user.getTitle());
    }
}
