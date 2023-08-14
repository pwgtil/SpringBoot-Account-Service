package account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class RoleOpsDTO {

    @NotEmpty(message = "Email cannot be empty (it's your username!)")
    @Email(regexp = "[\\w.]+(@acme.com)", message = "Only acme.com domain please")
    private String user;

    @NotEmpty(message = "Role cannot be empty (ROLE_ADMINISTRATOR, ROLE_USER or ROLE_ACCOUNTANT)")
    private String role;

    @NotEmpty(message = "Operation cannot be empty (GRANT or REMOVE)")
    private String operation;

    public RoleOpsDTO(String user, String role, String operation) {
        this.user = user;
        this.role = role;
        this.operation = operation;
    }

    public RoleOpsDTO() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        role = role.toUpperCase();
        String pattern = "\\bROLE_";
        if (!role.matches(pattern)){
            role = "ROLE_" + role;
        }
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
