package org.badges.api.domain.news;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = {"entityId", "comment", "imageUrl"})
public class AchtungNewsDto {

    private Long id;

    private String name;

    private String comment;

    private String imageUrl;

    private Long entityId;

    private ActionRequiredType actionRequired;

    private List<UserNewsDto> toUsers;
}
