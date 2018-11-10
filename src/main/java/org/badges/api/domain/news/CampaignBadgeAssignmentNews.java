package org.badges.api.domain.news;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CampaignBadgeAssignmentNews {
    private BadgeNewsDto badge;

    private List<UserNewsDto> toUsers;

    private UserNewsDto assigner;

    private String comment;

    private Date date;

}
