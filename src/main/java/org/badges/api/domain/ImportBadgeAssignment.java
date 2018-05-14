package org.badges.api.domain;

import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ImportBadgeAssignment {

    private Set<Long> employeesIds;

    private Long badgeId;

    private String comment;

    private Long assignerId;

    private List<String> tags;

    public ImportBadgeAssignment addEmployees(Long... ids) {
        if (employeesIds == null) {
            employeesIds = new HashSet<>(ids.length);
        }
        Collections.addAll(employeesIds, ids);
        return this;
    }
}
