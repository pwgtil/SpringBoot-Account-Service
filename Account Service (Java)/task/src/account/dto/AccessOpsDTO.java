package account.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessOpsDTO {

    @NotEmpty(message = "Email cannot be empty (it's your username!)")
    private String user;

    @NotEmpty(message = "Operation cannot be empty (LOCK or UNLOCK)")
    private String operation;

    public String getUser() {
        return this.user.toLowerCase();
    }
}
