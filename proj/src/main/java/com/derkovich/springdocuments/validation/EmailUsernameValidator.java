package com.derkovich.springdocuments.validation;

import com.derkovich.springdocuments.service.dto.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailUsernameValidator implements ConstraintValidator<EmailUsername, String> {
    private User user;

    public EmailUsernameValidator(User user) {
        this.user = user;
    }

    public EmailUsernameValidator(){}

    public void initialize(EmailUsername constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && Pattern.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", username);
    }
}
