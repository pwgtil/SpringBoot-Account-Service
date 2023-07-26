package account.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class PasswordService {
    static final List<String> BREACHED_PASSWORDS = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    static final int PASSWORD_LENGTH = 12;

    private PasswordEncoder passwordEncoder;

    public PasswordService() {
        passwordEncoder = new BCryptPasswordEncoder(13);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public boolean hasValidLength(String password){
        return password.length() >= PASSWORD_LENGTH;
    }

    public boolean isSafe(String password) {
        return !BREACHED_PASSWORDS.contains(password);
    }

    public boolean isNotPreviousPassword(String newPassword, String hash) {
        return !passwordEncoder.matches(newPassword, hash);
    }


}
