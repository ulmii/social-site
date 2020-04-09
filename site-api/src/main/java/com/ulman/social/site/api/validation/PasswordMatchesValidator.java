package com.ulman.social.site.api.validation;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object>
{
    private UserService userService;

    @Override
    public void initialize(PasswordMatches constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context)
    {
        UserDto user = (UserDto) obj;
        //        UserDto = userService.getUser(user.getId());
        //        return user.getPassword().equals(user.getMatchingPassword());
        return true;
    }
}
