package account.controller;

import account.controller.routing.Role;
import account.controller.routing.User;
import account.dto.RoleOpsDTO;
import account.dto.response.UserDeletionStatusDTO;
import account.service.UserService;
import account.dto.PasswordDTO;
import account.dto.UserDTO;
import account.controller.routing.Signup;
import account.controller.routing.ChangePass;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
public class UserController {

    final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping(Signup.PATH)
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO dto = UserDTO.convertUserToDTO(userService.signUp(userDTO.convertDTOToUser()));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(ChangePass.PATH)
    public ResponseEntity<PasswordDTO> changePassword(@Valid @RequestBody PasswordDTO passwordDTO, @AuthenticationPrincipal UserDetails details) {

        userService.changePass(details.getUsername(), passwordDTO.getPassword());

        passwordDTO.setEmail(details.getUsername());
        passwordDTO.setStatus("The password has been updated successfully");

        return new ResponseEntity<>(passwordDTO, HttpStatus.OK);
    }

    @GetMapping(User.PATH)
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsersData() {
        List<UserDTO> users = userService.getAllUsers();
        return users;
    }

    @DeleteMapping(User.PATH + "/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserDeletionStatusDTO deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new UserDeletionStatusDTO(email, "Deleted successfully!");
    }

    @PutMapping(Role.PATH)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO changeUserRoles(@Valid @RequestBody RoleOpsDTO roleOperation) {
        UserDTO user = userService.changeRole(roleOperation.getUsername(), roleOperation.getRole(), roleOperation.getOperation());
        return user;
    }
}
