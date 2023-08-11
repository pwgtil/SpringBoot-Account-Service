package account.service;

import account.dto.UserDTO;
import account.entity.Group;
import account.entity.User;
import account.repository.GroupRepository;
import account.repository.UserRepository;
import account.security.RolesManager;
import account.security.UserRole;
import account.security.UserRoleOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("userService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final RolesManager rolesManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordService passwordService,
                       RolesManager rolesManager) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.rolesManager = rolesManager;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByEmailIgnoreCase(username);
    }

    public User save(User userInput) {
        if (userRepository.count() == 0) {
            userInput.setUserGroups(rolesManager.processRole(
                    userInput.getUserGroups(),
                    UserRole.ROLE_ADMINISTRATOR.name(),
                    UserRoleOps.GRANT.name()));
        } else {
            userInput.setUserGroups(rolesManager.processRole(
                    userInput.getUserGroups(),
                    UserRole.ROLE_USER.name(),
                    UserRoleOps.GRANT.name()));
        }
        User dbUser = userRepository.findUserByEmailIgnoreCase(userInput.getEmail());
        if (dbUser != null) {
            userInput.setID(dbUser.getId());
        }
        return userRepository.save(userInput);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.withUsername(username)
                    .password(user.getPassword())
                    .authorities(user.getAuthorities())
                    .build();
        } else {
            return null;
        }
    }

    private Collection<GrantedAuthority> getAuthorities(User user) {
        Set<Group> userGroups = user.getUserGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
        for (Group group : userGroups) {
            authorities.add(new SimpleGrantedAuthority(group.getCode().toUpperCase()));
        }
        return authorities;
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

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userList.add(UserDTO.convertUserToDTO(user));
        }
        return userList.stream().toList();
    }

    public void deleteUser(String email) {
    }

    public UserDTO changeRole(String username, UserRole role, UserRoleOps operation) {
        return new UserDTO();
    }
}
