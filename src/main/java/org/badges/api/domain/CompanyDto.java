package org.badges.api.domain;

import lombok.Data;
import org.badges.db.BaseEntity;

@Data
public class CompanyDto extends BaseEntity {

    private Long id;

    private String name;

    private String imageUrl;

    @Override
    public Long getId() {
        return id;
    }
}
