package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.api.controller.query.UsersQueryParams;
import org.badges.api.domain.CurrentUser;
import org.badges.api.domain.UserDto;
import org.badges.db.repository.UserRepository;
import org.badges.service.converter.UserConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    @GetMapping
    public List<UserDto> search(UsersQueryParams usersQueryParams) {
        return userRepository.findByNameContainingIgnoreCaseAndEnabledIsTrue(usersQueryParams.getName())
                .stream()
                .skip(usersQueryParams.getOffset())
                .limit(usersQueryParams.getSize())
                .map(userConverter::convertUser)
                .collect(Collectors.toList());
    }

    @GetMapping("/current")
    public CurrentUser getById(@RequestParam("id") long id) {
        return userConverter.currentUser(userRepository.getOne(id));
    }
}
