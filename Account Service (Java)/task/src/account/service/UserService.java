package account.service;

import account.authorization.RolesManager;
import account.authorization.UserRole;
import account.authorization.UserRoleOps;
import account.dto.UserDTO;
import account.entity.User;
import account.entity.enums.ActionType;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.List;

@Service("userService")
public class UserService implements UserDetailsService, UserServiceGetInfo, UserServiceChangeAccess {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final RolesManager rolesManager;
    private final EventLogServicePostEvent eventLogService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordService passwordService, RolesManager rolesManager, EventLogServicePostEvent eventLogService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.rolesManager = rolesManager;
        this.eventLogService = eventLogService;
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
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .authorities(user.getAuthorities())
                    .accountLocked(!user.isAccountNonLocked())
                    .build();
        } else {
            eventLogService.postEvent(ActionType.LOGIN_FAILED, username, "", "");
            return null;
        }
    }

    public User signUp(User user) {

        // Basic check if exists in DB
        if (findUserByUsername(user.getEmail()) != null) {
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
        return userRepository.findAll()
                .stream()
                .map(UserDTO::convertUserToDTO)
                .toList();
//        List<UserDTO> userList = new ArrayList<>();
//        for (User user : userRepository.findAll()) {
//            userList.add(UserDTO.convertUserToDTO(user));
//        }
//        return userList.stream().toList();
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
    }

    public UserDTO changeRole(String username, String role, String operation) {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        user.setUserGroups(rolesManager.processRole(user.getUserGroups(), role, operation));

        save(user);

        return UserDTO.convertUserToDTO(user);
    }

    @Override
    public void changeAccess(String username, String operation) {
        operation = validateAccessOperation(operation);
        if (operation == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect operation. Should be LOCK or UNLOCK");
        }
        User user = findUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        if (rolesManager.isAdministrator(user.getUserGroups())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }

        switch (operation) {
            case "LOCK" -> user.setAccountNonLocked(false);
            case "UNLOCK" -> user.setAccountNonLocked(true);
        }

        save(user);
    }

    @Override
    public int getFailedLoginAttempts(String username) {
        return userRepository.getFailedAttempts(username);
    }

    @Override
    @Transactional
    public void setFailedLoginAttempts(int failedAttempts, String username) {
        int updatedRecords = userRepository.setFailedAttempts(failedAttempts, username);
    }


    private String validateAccessOperation(String operation) {
        operation = operation.toUpperCase();
        if (List.of("LOCK", "UNLOCK").contains(operation)) {
            return operation;
        } else {
            return null;
        }
    }

    @Override
    public UserGetInfo getUserInfo(String username) {
        return findUserByUsername(username);
    }
}
