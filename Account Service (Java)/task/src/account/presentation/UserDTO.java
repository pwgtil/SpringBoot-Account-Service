package account.presentation;

import account.businesslayer.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    /*
     * Static method (convertor)
     * */
    static public UserDTO convertUserToDTO(User user) {
        if (user != null) {
            return new UserDTO(user.getId(), user.getName(), user.getLastname(), user.getEmail(), user.getPassword());
        } else return null;
    }

    /*
     * Properties
     * */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Lastname cannot be empty")
    private String lastname;

    @NotEmpty(message = "Email cannot be empty (it's your username!)")
    @Email(regexp = "[\\w.]+(@acme.com)", message = "Only acme.com domain please")
    private String email;

    @NotEmpty(message = "Password needs to be filled in. Minimum 12 chars please.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /*
     * Constructors
     * */
    public UserDTO(Long id, String name, String lastname, String email, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
    }

    /*
     * Getters, Setters, Convertors
     * */
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

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                "\"name\":\"" + name + "\"" +
                ", \"lastname\":\"" + lastname + "\"" +
                ", \"email\":\"" + email + "\"" +
                "}";
    }
}
