package org.badges.api.domain;

import lombok.Data;

import java.util.Set;

@Data
public class ImportBadgeAssignment {

    private Set<Long> employeesIds;

    private Long badgeId;

    private String comment;
}
