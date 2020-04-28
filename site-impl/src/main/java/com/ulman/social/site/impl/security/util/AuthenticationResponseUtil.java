package com.ulman.social.site.impl.security.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulman.social.site.impl.security.response.model.JsonResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationResponseUtil
{
    public static ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static void sendJsonResponse(HttpServletResponse response, JsonResponse jsonResponse) throws IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(jsonResponse.getStatusType().getStatusCode());

        out.print(objectMapper.writeValueAsString(jsonResponse.getObject()));
        out.flush();
    }
}
