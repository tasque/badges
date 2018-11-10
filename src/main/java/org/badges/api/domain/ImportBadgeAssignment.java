package org.badges.api.domain;

import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
public class ImportBadgeAssignment {

    private Set<Long> usersIds;

    private Long badgeId;

    private String comment;

    public ImportBadgeAssignment addUsers(Long... ids) {
        if (usersIds == null) {
            usersIds = new HashSet<>(ids.length);
        }
        Collections.addAll(usersIds, ids);
        return this;
    }
}
