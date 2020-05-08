package com.ulman.social.site.impl.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OffsetPageRequest extends PageRequest
{
    private int offset;

    public OffsetPageRequest(int offset, int limit)
    {
        super(offset, limit, Sort.by(Sort.Direction.DESC, "created"));
        this.offset = offset;
    }

    @Override
    public long getOffset()
    {
        return this.offset;
    }
}
