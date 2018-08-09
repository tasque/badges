package org.badges.api.controller.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class NewsQueryParams implements Pageable {

    private int page;

    private int size = 20;

    private long userId;

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public int getOffset() {
        return page * size;
    }

    @Override
    public Sort getSort() {
        return new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
    }

    @Override
    public Pageable next() {
        return new NewsQueryParams().setPage(page + 1).setSize(size);
    }

    @Override
    public Pageable previousOrFirst() {
        return page == 0
                ? this
                : new NewsQueryParams().setPage(page - 1).setSize(0);
    }

    @Override
    public Pageable first() {
        return new NewsQueryParams().setPage(0).setSize(size);
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }
}
