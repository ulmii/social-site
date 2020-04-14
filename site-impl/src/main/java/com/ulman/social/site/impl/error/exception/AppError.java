package com.ulman.social.site.impl.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class AppError
{
    private String apiVersion;
    private ErrorBlock error;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;

    private AppError()
    {
        timestamp = LocalDateTime.now();
    }

    public AppError(final String apiVersion, final Response.StatusType statusType, final String message, final String domain,
            final String reason, final String errorMessage)
    {
        this();
        this.apiVersion = apiVersion;
        this.error = new ErrorBlock(UUID.randomUUID(), statusType.getStatusCode(), statusType, message, domain, reason, errorMessage);
    }

    public AppError(final String apiVersion, final Response.StatusType statusType, final String message, final List<Error> errors)
    {
        this();
        this.apiVersion = apiVersion;
        this.error = new ErrorBlock(UUID.randomUUID(), statusType.getStatusCode(), statusType, message, errors);
    }

    @Getter
    private static final class ErrorBlock
    {
        @JsonIgnore
        private final UUID uniqueId;
        private final int code;
        private final Response.StatusType status;
        private final String message;
        private final List<Error> errors;

        public ErrorBlock(final UUID uniqueId, final int code, final Response.StatusType status, final String message, final String domain,
                final String reason, final String errorMessage)
        {
            this.code = code;
            this.status = status;
            this.message = message;
            this.uniqueId = uniqueId;
            this.errors = List.of(
                    new Error(domain, reason, errorMessage)
            );
        }

        public ErrorBlock(final UUID uniqueId, final int code, final Response.StatusType statusType, final String message, final List<Error> errors)
        {
            this.uniqueId = uniqueId;
            this.code = code;
            this.status = statusType;
            this.message = message;
            this.errors = errors;
        }

        public static ErrorBlock copyWithMessage(final ErrorBlock s, final String message)
        {
            return new ErrorBlock(s.uniqueId, s.code, s.status, message, s.errors);
        }
    }

    @Getter
    public static final class Error
    {
        private final String domain;
        private final String reason;
        private final String message;

        public Error(final String domain, final String reason, final String message)
        {
            this.domain = domain;
            this.reason = reason;
            this.message = message;
        }
    }
}
