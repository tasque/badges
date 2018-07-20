package org.badges.service.converter;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.admin.AdminBadge;
import org.badges.db.Badge;
import org.badges.security.RequestContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeConverter {

    private final RequestContext requestContext;

    public AdminBadge convert(Badge badge) {
        return new AdminBadge()
                .setId(badge.getId())
                .setEnabled(badge.isEnabled())
                .setName(badge.getName())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl())
                .setVersion(badge.getVersion());
    }


    public Badge convert(AdminBadge badge) {
        return new Badge()
                .setId(badge.getId())
                .setEnabled(badge.isEnabled())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl())
                .setName(badge.getName())
                .setVersion(badge.getVersion());
    }

}
