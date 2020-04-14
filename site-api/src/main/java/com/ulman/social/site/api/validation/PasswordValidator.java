package com.ulman.social.site.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

        return false;
    }
}
