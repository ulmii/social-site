package com.ulman.social.site.impl.domain.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialBlob;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class ImageMapper
{
    public String blobToStringMapper(Blob blob)
    {
        if(Objects.isNull(blob))
        {
            return null;
        }

        try
        {
            return new String(blob.getBytes(1L, (int) blob.length()), StandardCharsets.UTF_8);
        }
        catch (SQLException e)
        {
            throw new RuntimeException();
        }
    }

    public Blob stringToBlobMapper(String image)
    {
        if(Objects.isNull(image) || image.isBlank())
        {
            return null;
        }

        try
        {
            return new SerialBlob(image.getBytes(StandardCharsets.UTF_8));
        }
        catch (SQLException e)
        {
            throw new RuntimeException();
        }
    }
}
