package org.badges.api.domain;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String title;

    private String imageUrl;
}
