package account.presentation;

import account.businesslayer.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDTO {

    static public UserDTO convertUserToDTO(User user) {
        if (user != null) {
            return new UserDTO(user.getId(), user.getName(), user.getLastname(), user.getEmail());
        } else return null;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

//    @NotEmpty(message = "Name cannot be blank")
    private String name;

//    @NotEmpty(message = "Lastname cannot be blank")
    private String lastname;

//    @Email(regexp = "[\\w.]+(@acme.com)", message = "Email wrong format")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public UserDTO(Long id, String name, String lastname, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    public UserDTO() {
    }

    public User convertDTOToUser() {
        return new User(this.getName(), this.getLastname(), this.getEmail().toLowerCase(), this.getPassword());
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
