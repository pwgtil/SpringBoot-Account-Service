package account.service;

import account.exception.BreachedPasswordException;
import account.exception.TooShortPasswordException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class PasswordService {
    /*
     * Statics
     * */
    static final List<String> BREACHED_PASSWORDS = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril", "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    static final int PASSWORD_LENGTH = 12;

    /*
     * Attributes
     * */
    private final PasswordEncoder passwordEncoder;

    /*
     * Constructors
     * */
    public PasswordService() {
        passwordEncoder = new BCryptPasswordEncoder(13);
    }


    /*
     * Methods
     * */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void checkValidity(String password) {

        // Check length
        if (hasInvalidLength(password)) {
            throw new TooShortPasswordException();
        }
        // Check if in hacker database
        if (isNotSafe(password)) {
            throw new BreachedPasswordException();
        }

    }

    public boolean isPreviousPassword(String newPassword, String hash) {
        return passwordEncoder.matches(newPassword, hash);
    }

    private boolean hasInvalidLength(String password) {
        return password.length() < PASSWORD_LENGTH;
    }

    private boolean isNotSafe(String password) {
        return BREACHED_PASSWORDS.contains(password);
    }

}
