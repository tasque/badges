package org.badges.api.controller.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class NewsQueryParams extends PageRequest {


    public NewsQueryParams(int page, int size) {
        super(page, size);
    }

    public NewsQueryParams(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public NewsQueryParams(int page, int size, Sort sort) {
        super(page, size, sort);
    }
}
