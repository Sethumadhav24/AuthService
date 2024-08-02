package org.example.utils;

import org.example.model.UserInfoDto;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%])[A-Za-z\\d@#$%]{8,}$");

    public boolean validateUserAttributes(UserInfoDto userInfoDto) {
        return isValidEmail(userInfoDto.getEmail()) && isValidPassword(userInfoDto.getPassword());
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches() &&
                (email.endsWith("gmail.com") || email.endsWith("yahoo.com") || email.endsWith("outlook.com"));
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches() && Character.isUpperCase(password.charAt(0));
    }
}
