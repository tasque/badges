package org.badges.api.domain.news;

import lombok.Data;
import org.badges.db.NewsType;

import java.util.Date;
import java.util.List;

@Data
public class NewsDto {

    private Long id;

    private Long entityId;

    private NewsType newsType;

    private String comment;

    private List<UserNewsDto> toUsers;

    private UserNewsDto author;

    private List<String> tags;

    private Date date;

    private Object reason;
}
