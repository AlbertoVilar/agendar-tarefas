package com.vilardev.Daily.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class BCryptUtil {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private BCryptUtil() {}

    public static String encode(String raw) {
        return ENCODER.encode(raw);
    }
}

