package org.badges.service;

import org.badges.db.User;
import org.badges.db.UserPermission;
import org.springframework.stereotype.Component;

@Component
public class UserRoleService {
    public boolean hasPermission(User user, UserPermission userPermissions) {
        return user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getUserPermissions().contains(userPermissions));
    }
}
