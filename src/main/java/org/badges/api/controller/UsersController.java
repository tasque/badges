package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.api.controller.query.UsersQueryParams;
import org.badges.api.domain.CurrentUser;
import org.badges.api.domain.UserDto;
import org.badges.db.User;
import org.badges.db.repository.UserRepository;
import org.badges.security.RequestContext;
import org.badges.service.BadgeAssignmentService;
import org.badges.service.converter.UserConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final RequestContext requestContext;

    private final BadgeAssignmentService badgeAssignmentService;


    @GetMapping("/search")
    public List<UserDto> search(UsersQueryParams usersQueryParams) {
        List<User> result = userRepository.findUsers(
                usersQueryParams.getName().trim(), requestContext.getCurrentUserId(), usersQueryParams.getSize());
        return badgeAssignmentService.filterUsers(result, usersQueryParams.getBadgeId())
                .stream()
                .map(userConverter::convertUser)
                .collect(Collectors.toList());
    }

    @GetMapping("/current")
    public CurrentUser currentUser() {
        return userConverter.currentUser(requestContext.getCurrentUser());
    }

    @GetMapping("/byIds")
    public List<UserDto> getByIds(Collection<Long> ids) {
        return userRepository.findAll(ids).stream()
                .map(userConverter::convertUser)
                .collect(Collectors.toList());
    }

}
