package com.ulman.social.site.impl.domain.error.exception.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulman.social.site.impl.domain.error.exception.model.JsonResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;

public class ResponseUtil
{
    public static ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static void sendJsonResponse(TimeZone timeZone, HttpServletResponse response, JsonResponse jsonResponse) throws IOException
    {
        objectMapper.setTimeZone(timeZone);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(jsonResponse.getStatusType().getStatusCode());

        out.print(objectMapper.writeValueAsString(jsonResponse.getObject()));
        out.flush();
    }
}
