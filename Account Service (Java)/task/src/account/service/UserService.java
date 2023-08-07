package account.service;

import account.entity.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;

@Service("userService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserService(@Autowired UserRepository userRepository, @Autowired PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByEmailIgnoreCase(username);
    }

    public User save(User userInput) {
        User dbUser = userRepository.findUserByEmailIgnoreCase(userInput.getEmail());
        if (dbUser != null) {
            userInput.setID(dbUser.getId());
        }
        return userRepository.save(userInput);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findUserByEmail(username.toLowerCase());
    }

    public User signUp(User user) {

        // Basic check if exists in DB
        if (loadUserByUsername(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }

        // Password format check
        try {
            passwordService.checkValidity(user.getPassword());
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        user.setPassword(passwordService.getPasswordEncoder().encode(user.getPassword()));

        save(user);

        return user;
    }

    public void changePass(String username, String password) {

        // Password format check
        try {
            passwordService.checkValidity(password);
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        // Check duplication
        User user = findUserByUsername(username);
        if (passwordService.isPreviousPassword(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        user.setPassword(passwordService.getPasswordEncoder().encode(password));

        save(user);
    }
}
