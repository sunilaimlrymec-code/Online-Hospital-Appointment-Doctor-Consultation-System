package com.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provides the BCrypt password encoder used to hash and verify
 * Patient and Doctor credentials.
 *
 * NOTE: This project intentionally does not pull in the full
 * spring-boot-starter-security auto-configuration. Authentication here
 * is deliberately session-based (HttpSession "user" / "doctor" attributes),
 * mirroring the original servlet design while replacing plain-text
 * passwords with BCrypt hashes. Only the crypto module is used.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
