package org.badges.api.domain;

import lombok.Data;
import org.badges.db.NewsType;

import java.util.List;

@Data
public class NewsDto {

    private Long id;

    private Long entityId;

    private NewsType newsType;

    private String comment;

    private List<EmployeeDto> toEmployees;

    private EmployeeDto author;

    private List<String> tags;
}
