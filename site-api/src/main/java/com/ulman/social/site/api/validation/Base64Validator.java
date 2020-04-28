package com.ulman.social.site.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class Base64Validator implements
        ConstraintValidator<Base64, String>
{
    @Override
    public void initialize(Base64 constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(String base64String, ConstraintValidatorContext context)
    {
        return Objects.isNull(base64String)
                || base64String.matches("^data:image/([a-zA-Z]+);base64,([^\"]*)");
    }
}
