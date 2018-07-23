package org.badges.api.domain.catalog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogBadge {
    private String name;

    private String description;

    private String category;

    private String imageUrl;
}
