package com.chat.backend.validations;

import com.chat.backend.dto.AccountRegistrationForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final AccountRegistrationForm acc = (AccountRegistrationForm) obj;
        return acc.getPassword().equals(acc.getConfirmationPassword());
    }
}
