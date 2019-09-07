package org.badges.api.domain.catalog;

import lombok.Data;

@Data
public class CatalogBadgeSettings {

    private Integer countLeft;

    private Integer toUsersMax;

    private boolean special;
}
