package org.badges.service.converter;

import lombok.RequiredArgsConstructor;
import org.badges.api.domain.CompanyDto;
import org.badges.api.domain.admin.AdminBadge;
import org.badges.db.Badge;
import org.badges.db.Company;
import org.badges.security.RequestContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeConverter {

    private final RequestContext requestContext;

    public AdminBadge convert(Badge badge) {
        Company company = badge.getCompany();
        return new AdminBadge()
                .setId(badge.getId())
                .setEnabled(badge.isEnabled())
                .setName(badge.getName())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl())
                .setVersion(badge.getVersion())
                .setCompany(new CompanyDto()
                        .setId(company.getId())
                        .setImageUrl(company.getImageUrl())
                        .setName(company.getName()));
    }


    public Badge convert(AdminBadge badge) {
        return new Badge()
                .setId(badge.getId())
                .setEnabled(badge.isEnabled())
                .setDescription(badge.getDescription())
                .setImageUrl(badge.getImageUrl())
                .setName(badge.getName())
                .setVersion(badge.getVersion())
                .setCompany(requestContext.getCurrentTenant());
    }

}
