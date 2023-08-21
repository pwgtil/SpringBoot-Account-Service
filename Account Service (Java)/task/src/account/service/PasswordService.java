package account.service;

import account.exception.BreachedPasswordException;
import account.exception.TooShortPasswordException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@Service
public class PasswordService {
    /*
     * Statics
     * */
    static final List<String> BREACHED_PASSWORDS = List.of();
    static final int PASSWORD_LENGTH = 12;

    /*
     * Attributes
     * */
    private final PasswordEncoder passwordEncoder;
    private final String fileName;
    private final int passwordLength;
    private List<String> breachedPasswords;

    /*
     * Constructors
     * */
    public PasswordService(
            @Value("${login.breached-passwords-file}") String fileName,
            @Value("${login.password-length}") int passwordLength) {
        this.passwordEncoder = new BCryptPasswordEncoder(13);
        this.fileName = fileName;
        this.passwordLength = passwordLength;
    }

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        URL dataFile = ClassLoader.getSystemResource(fileName);
        breachedPasswords = Files.readAllLines(Path.of(dataFile.toURI()));
        if (!breachedPasswords.isEmpty()) {
            log.warn("Breached passwords library loaded. {} entries acquired.", breachedPasswords.size());
        } else {
            log.warn("Breached passwords library not loaded");
        }
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
        return password.length() < passwordLength;
    }

    private boolean isNotSafe(String password) {
        return breachedPasswords.contains(password);
    }

}
