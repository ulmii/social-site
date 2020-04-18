package com.ulman.social.site.api.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordValidator implements
        ConstraintValidator<Password, String>
{
    @Override
    public void initialize(Password constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context)
    {
        if(Objects.isNull(password))
        {
            return true;
        }

        context.disableDefaultConstraintViolation();

        if(password.strip().length() < 6)
        {
            context.buildConstraintViolationWithTemplate("Password must be at least 6 characters long")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
