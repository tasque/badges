package org.badges.api.controller.admin;


import lombok.RequiredArgsConstructor;
import org.badges.db.UserPermission;
import org.badges.db.UserRole;
import org.badges.db.repository.UserRoleRepository;
import org.badges.security.annotation.RequiredPermission;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users/roles")
@RequiredArgsConstructor
public class UsersRoleController {

    private final UserRoleRepository userRoleRepository;

    @RequiredPermission(UserPermission.READ_USER_ROLE)
    @GetMapping
    public List<UserRole> getUserRoles() {
        return userRoleRepository.findAll();
    }

    @RequiredPermission(UserPermission.READ_USER_ROLE)
    @GetMapping("/{id}")
    public UserRole getUserRole(@PathVariable("id") long id) {
        return userRoleRepository.getOne(id);
    }


    @RequiredPermission(UserPermission.UPDATE_USER_ROLE)
    @PostMapping
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }


    @RequiredPermission(UserPermission.DELETE_USER_ROLE)
    @DeleteMapping
    public void delete(long id) {
        userRoleRepository.delete(id);
    }

    @GetMapping("/permissions")
    public UserPermission[] getUserPermissions() {
        return UserPermission.values();
    }
}
