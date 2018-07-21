package org.badges.api.domain;

import lombok.Data;
import org.badges.db.UserPermission;

import java.util.List;

@Data
public class CurrentUser {

    private Long id;

    private String name;

    private String email;

    private String title;

    private String imageUrl;

    private List<UserPermission> userPermissions;
}
