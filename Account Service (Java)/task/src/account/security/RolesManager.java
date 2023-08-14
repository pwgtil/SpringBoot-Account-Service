package account.security;

import account.entity.Group;
import account.repository.GroupRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class RolesManager {

    private Set<Group> currentRoles;
    private Set<Group> allRoles;
    private UserRole role;
    private final GroupRepository groupRepository;

    private RolesManager(@Autowired GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
//        allRoles = (Set<Group>) groupRepository.findAll();
    }

    public Set<Group> processRole(Set<Group> currentRoles, @NotNull String role2Process, @NotNull String operation2Process) {

        this.currentRoles = currentRoles;

        try {
            role = UserRole.valueOf(role2Process.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        UserRoleOps operation;

        try {
            operation = UserRoleOps.valueOf(operation2Process.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation not found!");
        }

        switch (role) {
            case ROLE_USER -> {
                switch (operation) {
                    case REMOVE -> {
                        removeValidation();
                        removeUser();
                    }
                    case GRANT -> {
                        grantValidationAdminVSBusinessRoles();
                        grantUser();
                    }
                }
            }
            case ROLE_ACCOUNTANT -> {
                switch (operation) {
                    case REMOVE -> {
                        removeValidation();
                        removeAccountant();
                    }
                    case GRANT -> {
                        grantValidationAdminVSBusinessRoles();
                        grantAccountant();
                    }
                }
            }
            case ROLE_ADMINISTRATOR -> {
                switch (operation) {
                    case REMOVE -> {
//                        removeValidation();
                        removeAdmin();
                    }
                    case GRANT -> {
                        grantValidationAdminVSBusinessRoles();
                        grantAdmin();
                    }
                }
            }
        }

        return this.currentRoles;

    }

    public boolean isAdministrator(Set<Group> currentRoles) {
        this.currentRoles = currentRoles;
        Group group = findRole(UserRole.ROLE_ADMINISTRATOR);
        return group != null;
    }


    private void removeValidation() {
        if (currentRoles.size() <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }
        if (!containsRole(role)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
    }

    private void grantAdmin() {
        currentRoles.add(groupRepository.findGroupByCode(UserRole.ROLE_ADMINISTRATOR.name()));
    }

    private void removeAdmin() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!"); // 400
    }

    private void grantValidationAdminVSBusinessRoles() {
        if (role == UserRole.ROLE_ADMINISTRATOR && !currentRoles.isEmpty() && !containsRole(UserRole.ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        } else if (!currentRoles.isEmpty() && containsRole(UserRole.ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }

    }

    private void grantAccountant() {
        currentRoles.add(groupRepository.findGroupByCode(UserRole.ROLE_ACCOUNTANT.name()));
    }

    private void removeAccountant() {
        Group group = findRole(UserRole.ROLE_ACCOUNTANT);
        if (group != null) {
            currentRoles.remove(group);
        }
    }

    private void grantUser() {
        currentRoles.add(groupRepository.findGroupByCode(UserRole.ROLE_USER.name()));
    }

    private void removeUser() {
        Group group = findRole(UserRole.ROLE_USER);
        if (group != null) {
            currentRoles.remove(group);
        }
    }

    private boolean containsRole(UserRole role) {
        for (Group group : currentRoles) {
            if (group.getCode().toUpperCase().equals(role.name())) {
                return true;
            }
        }
        return false;
    }

    private Group findRole(UserRole role) {
        for (Group group : currentRoles) {
            if (group.getCode().toUpperCase().equals(role.name())) {
                return group;
            }
        }
        return null;
    }
}
