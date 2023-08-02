package account.businesslayer;

import account.entity.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public User save(User userInput) {
        User dbUser = userRepository.findUserByEmailIgnoreCase(userInput.getEmail());
        if (dbUser == null) {
            User user = userRepository.save(userInput);
            return user;
        } else {
            userInput.setID(dbUser.getId());
            User user = userRepository.save(userInput);
            return user;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findUserByEmail(username.toLowerCase());
    }
}
