package account.service;

import account.dto.UserDTO;
import account.entity.User;
import account.repository.UserRepository;
import account.authorization.RolesManager;
import account.authorization.UserRole;
import account.authorization.UserRoleOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserService implements UserDetailsService, UserServiceGetInfo {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final RolesManager rolesManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordService passwordService, RolesManager rolesManager) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.rolesManager = rolesManager;
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    private User findUserByUsername(String username) {
        return userRepository.findUserByEmailIgnoreCase(username);
    }

    private void setBaseRole(User user) {
        if (userRepository.count() == 0) {
            user.setUserGroups(rolesManager.processRole(user.getUserGroups(), UserRole.ROLE_ADMINISTRATOR.name(), UserRoleOps.GRANT.name()));
        } else {
            user.setUserGroups(rolesManager.processRole(user.getUserGroups(), UserRole.ROLE_USER.name(), UserRoleOps.GRANT.name()));
        }
    }

    private void save(User userInput) {

        User dbUser = userRepository.findUserByEmailIgnoreCase(userInput.getEmail());
        if (dbUser != null) {
            userInput.setID(dbUser.getId());
        }
        userRepository.save(userInput);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword()).authorities(user.getAuthorities()).build();
        } else {
            return null;
        }
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

        // Set base role. If first user in DB -> Admin, others Users
        setBaseRole(user);

        user.setPassword(passwordService.getPasswordEncoder().encode(user.getPassword()));

        save(user);
        // EVENTLOG: CREATE_USER

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

        // EVENT_LOG: CHANGE_PASSWORD
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userList.add(UserDTO.convertUserToDTO(user));
        }
        return userList.stream().toList();
    }

    public void deleteUser(String email) {
        User user = findUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        if (rolesManager.isAdministrator(user.getUserGroups())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        // EVENT_LOG: DELETE_USER
    }

    public UserDTO changeRole(String username, String role, String operation) {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        user.setUserGroups(rolesManager.processRole(user.getUserGroups(), role, operation));
        // EVENT_LOG: GRANT_ROLE or REMOVE_ROLE

        save(user);

        return UserDTO.convertUserToDTO(user);
    }

    @Override
    public UserGetInfo getUserInfo(String username) {
        return findUserByUsername(username);
    }
}
