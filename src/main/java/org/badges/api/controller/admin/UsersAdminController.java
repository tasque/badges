package org.badges.api.controller.admin;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.UserDto;
import org.badges.api.domain.admin.AdminUser;
import org.badges.db.User;
import org.badges.db.UserPermission;
import org.badges.db.repository.UserRepository;
import org.badges.security.annotation.RequiredPermission;
import org.badges.service.converter.UserConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class UsersAdminController {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    @RequiredPermission(UserPermission.READ_USERS)
    @GetMapping
    public Page<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userConverter::convertUser);
    }

    @RequiredPermission(UserPermission.READ_USERS)
    @GetMapping("/{id}")
    public AdminUser getUser(@PathVariable("id") long id) {
        User user = userRepository.getOne(id);
        return new AdminUser()
                .setId(user.getId())
                .setName(user.getName());
    }
}
