package account.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RoleOpsDTO {

    @NotEmpty(message = "Email cannot be empty (it's your username!)")
//    @Email(regexp = "[\\w.]+(@acme.com)", message = "Only acme.com domain please")
    private String user;

    @NotEmpty(message = "Role cannot be empty (ROLE_ADMINISTRATOR, ROLE_USER or ROLE_ACCOUNTANT)")
    private String role;

    @NotEmpty(message = "Operation cannot be empty (GRANT or REMOVE)")
    private String operation;

    public RoleOpsDTO(String user, String role, String operation) {
        setUser(user);
        setRole(role);
        setOperation(operation);
    }

    public String getUser() {
        return user.toLowerCase();
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRole(String role) {
        role = role.toUpperCase();
        String pattern = "\\bROLE_";
        if (!role.matches(pattern)){
            role = "ROLE_" + role;
        }
        this.role = role;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
