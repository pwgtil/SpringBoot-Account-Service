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
            return new UserDTO(user.getId(), user.getName(), user.getLastname(), user.getEmail());
        } else return null;
    }

    /*
     * Properties
     * */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String lastname;

    @NotEmpty
    @NotNull
    @Email(regexp = "[\\w.]+(@acme.com)")
    private String email;

    @NotEmpty
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /*
     * Constructors
     * */
    public UserDTO(Long id, String name, String lastname, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
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
