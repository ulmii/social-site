package com.ulman.social.site.impl.error.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

public class AppError
{
    private final String apiVersion;
    private final ErrorBlock error;
    private final Response.StatusType statusType;
    public AppError(final String apiVersion, final Response.StatusType statusType, final String message, final String domain,
            final String reason, final String errorMessage)
    {
        this.statusType = statusType;
        this.apiVersion = apiVersion;
        this.error = new ErrorBlock(statusType.getStatusCode(), message, domain, reason, errorMessage);
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public ErrorBlock getError()
    {
        return error;
    }

    @JsonIgnore
    public Response.StatusType getStatusType()
    {
        return statusType;
    }

    private static final class ErrorBlock
    {

        @JsonIgnore
        private final UUID uniqueId;
        private final int code;
        private final String message;
        private final List<Error> errors;

        public ErrorBlock(final int code, final String message, final String domain,
                final String reason, final String errorMessage)
        {
            this.code = code;
            this.message = message;
            this.uniqueId = UUID.randomUUID();
            this.errors = List.of(
                    new Error(domain, reason, errorMessage)
            );
        }

        private ErrorBlock(final UUID uniqueId, final int code, final String message, final List<Error> errors)
        {
            this.uniqueId = uniqueId;
            this.code = code;
            this.message = message;
            this.errors = errors;
        }

        public static ErrorBlock copyWithMessage(final ErrorBlock s, final String message)
        {
            return new ErrorBlock(s.uniqueId, s.code, message, s.errors);
        }

        public UUID getUniqueId()
        {
            return uniqueId;
        }

        public int getCode()
        {
            return code;
        }

        public String getMessage()
        {
            return message;
        }

        public List<Error> getErrors()
        {
            return errors;
        }

    }

    private static final class Error
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

        public String getDomain()
        {
            return domain;
        }

        public String getReason()
        {
            return reason;
        }

        public String getMessage()
        {
            return message;
        }
    }
}
