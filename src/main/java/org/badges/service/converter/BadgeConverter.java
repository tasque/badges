package org.badges.service.converter;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.admin.AdminBadge;
import org.badges.api.domain.catalog.CatalogBadge;
import org.badges.db.Badge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeConverter {

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

    public CatalogBadge catalogBadge(Badge badge) {
        return new CatalogBadge()
                .setCategory(badge.getCategory())
                .setName(badge.getName())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl());
    }


}
