package ru.practicum.shareit.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ShareItPageRequest extends PageRequest {

    private final int from;

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param from zero-based page index, must not be negative.
     * @param size the size of the page to be returned, must be greater than 0.
     * @param sort must not be {@literal null}, use {@link Sort#unsorted()} instead.
     */
    public ShareItPageRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    @Override
    public long getOffset() {
        return from;
    }


}
