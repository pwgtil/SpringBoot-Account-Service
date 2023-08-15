package account.controller;

import account.controller.routing.*;
import account.dto.RoleOpsDTO;
import account.dto.response.StatusResponse;
import account.service.UserService;
import account.dto.PasswordDTO;
import account.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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

    @GetMapping(User.PATH + "/")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsersData() {
        return userService.getAllUsers();
    }

    @DeleteMapping(User.PATH + "/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return Map.of("user", email, "status", "Deleted successfully!");
    }

    @PutMapping(Role.PATH)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO changeUserRoles(@Valid @RequestBody RoleOpsDTO roleOperation) {
        return userService.changeRole(roleOperation.getUser(), roleOperation.getRole(), roleOperation.getOperation());
    }

    @PutMapping(Access.PATH)
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse changeUserAccess() {
        // TODO(
        //  1. Add arguments block
        //  2. Check operation LOCK/UNLOCK
        //  3. Check if i'm admin
        //  4. Add EVENT_LOG: LOCK_USER / UNLOCK_USER stuff
        //  5. Actually change my lock)
        return new StatusResponse("User user@acme.com locked!");
    }
}
