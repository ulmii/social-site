package com.ulman.social.site.impl.domain.handler;

import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.AppError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Provider
@Component
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException>
{
    public static final Response.Status STATUS = Response.Status.BAD_REQUEST;
    private EnvironmentProperties environmentProperties;

    @Autowired
    public ConstraintViolationExceptionHandler(EnvironmentProperties environmentProperties)
    {
        this.environmentProperties = environmentProperties;
    }

    @Override
    public Response toResponse(ConstraintViolationException e)
    {
        final AppError error = new AppError(
                environmentProperties.getTimeZone().toZoneId(),
                environmentProperties.getApiVersion(),
                STATUS,
                e.getMessage(),
                prepareErrors(e)
        );

        return Response.status(STATUS)
                .entity(error)
                .build();
    }

    private List<AppError.Error> prepareErrors(ConstraintViolationException exception)
    {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        return constraintViolations.stream()
                .map(constraintViolation -> new AppError.Error(
                        "User",
                        constraintViolation.getConstraintDescriptor()
                                .getConstraintValidatorClasses().get(0)
                                .getSimpleName(),
                        String.format("%s: value '%s' does not pass %s validation",
                                constraintViolation.getMessage(),
                                constraintViolation.getInvalidValue(),
                                StreamSupport.stream(constraintViolation.getPropertyPath()
                                        .spliterator(), false)
                                        .reduce((first, second) -> second)
                                        .orElse(null))))
                .collect(Collectors.toList());
    }
}
