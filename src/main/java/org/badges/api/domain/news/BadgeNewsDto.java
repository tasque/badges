package org.badges.api.domain.news;

import lombok.Data;

@Data
public class BadgeNewsDto {

    private Long id;

    private String name;

//    private String description;

    private String imageUrl;
}
