package org.badges.service.converter;

import org.badges.api.domain.news.EmployeeNewsDto;
import org.badges.db.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter {

    public EmployeeNewsDto convert(Employee employee) {
        return new EmployeeNewsDto()
                .setId(employee.getId())
                .setImageUrl(employee.getImageUrl())
                .setName(employee.getName());
    }
}
