package com.vilardev.Daily.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "132747";
        String hash = encoder.encode(raw);
        System.out.println(hash);
    }
}

