package org.badges.api.domain.admin;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@Data
public class AdminBadge {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String imageUrl;

    private boolean enabled;

    @NotNull
    @Min(0)
    private Integer version;
}
