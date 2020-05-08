package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.error.exception.PaginationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OffsetPageRequest extends PageRequest
{
    private int offset;

    private OffsetPageRequest(int limit, int offset)
    {
        super(offset, limit, Sort.by(Sort.Direction.DESC, "created"));
        this.offset = offset;
    }

    private OffsetPageRequest(int limit, int offset, Sort sort)
    {
        super(offset, limit, sort);
        this.offset = offset;
    }

    public static OffsetPageRequest of(int limit, int offset)
    {
        try
        {
            return new OffsetPageRequest(limit, offset);
        }
        catch (IllegalArgumentException e)
        {
            throw new PaginationException(e.getMessage());
        }
    }

    public static OffsetPageRequest of(int limit, int offset, Sort sort)
    {
        try
        {
            return new OffsetPageRequest(limit, offset, sort);
        }
        catch (IllegalArgumentException e)
        {
            throw new PaginationException(e.getMessage());
        }
    }

    @Override
    public long getOffset()
    {
        return this.offset;
    }
}
