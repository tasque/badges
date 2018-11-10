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
        List<User> result = userRepository.findByNameContainingIgnoreCaseAndEnabledIsTrueAndIdIsNot(
                usersQueryParams.getName().trim(), requestContext.getCurrentUserId());
        return badgeAssignmentService.filterUsers(result, usersQueryParams.getBadgeId())
                .stream()
                .limit(usersQueryParams.getSize())
                .map(userConverter::convertUser)
                .collect(Collectors.toList());
    }

    @GetMapping("/current")
    public CurrentUser currentUser() {
        return userConverter.currentUser(requestContext.getCurrentUser());
    }


}
