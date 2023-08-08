package account.dto;

import account.security.UserRole;
import account.security.UserRoleOps;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class RoleOpsDTO {

    @NotEmpty(message = "Email cannot be empty (it's your username!)")
    @Email(regexp = "[\\w.]+(@acme.com)", message = "Only acme.com domain please")
    private String username;

    @NotEmpty(message = "Role cannot be empty (ROLE_ADMINISTRATOR, ROLE_USER or ROLE_ACCOUNTANT)")
    private UserRole role;

    @NotEmpty(message = "Operation cannot be empty (GRANT or REMOVE)")
    private UserRoleOps operation;

    public RoleOpsDTO(String username, UserRole role, UserRoleOps operation) {
        this.username = username;
        this.role = role;
        this.operation = operation;
    }

    public RoleOpsDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRoleOps getOperation() {
        return operation;
    }

    public void setOperation(UserRoleOps operation) {
        this.operation = operation;
    }
}
