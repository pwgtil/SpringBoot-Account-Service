package account.presentation;

import account.businesslayer.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDTO {

    @NotEmpty(message = "Name cannot be blank")
    private String name;

    @NotEmpty(message = "Lastname cannot be blank")
    private String lastname;

    @Email(regexp = "[\\w.]+(@acme.com)", message = "Email wrong format")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public UserDTO(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
    }

    public User convertDTOToUser() {
        return new User(this.getName(), this.getLastname(), this.getEmail(), this.getPassword());
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
