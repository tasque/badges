package org.badges.api.controller.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersQueryParams {

    private String name = "";

    private int size = 10;

    private Long badgeId;

}
