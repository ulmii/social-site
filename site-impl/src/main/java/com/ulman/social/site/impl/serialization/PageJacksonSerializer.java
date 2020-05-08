package com.ulman.social.site.impl.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.io.Serializable;

@JsonComponent
public class PageJacksonSerializer extends JsonSerializer<Page<? extends Serializable>>
{
    @Override
    public void serialize(Page page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
    {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("content", page.getContent());
        jsonGenerator.writeNumberField("_count", page.getNumberOfElements());
        jsonGenerator.writeNumberField("_limit", page.getSize());
        jsonGenerator.writeNumberField("_offset", page.getPageable().getOffset());
        jsonGenerator.writeNumberField("_totalElements", page.getTotalElements());
        jsonGenerator.writeBooleanField("_first", page.isFirst());
        jsonGenerator.writeBooleanField("_last", page.isLast());

        Sort sort = page.getSort();
        jsonGenerator.writeArrayFieldStart("_sort");

        for (Sort.Order order : sort)
        {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeBooleanField("sorted", sort.isSorted());
            jsonGenerator.writeStringField("property", order.getProperty());
            jsonGenerator.writeStringField("direction", order.getDirection().name());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }
}
