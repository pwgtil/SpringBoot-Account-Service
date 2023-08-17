package account.controller;

import account.controller.routing.*;
import account.dto.AccessOpsDTO;
import account.dto.RoleOpsDTO;
import account.dto.response.StatusResponse;
import account.entity.enums.ActionType;
import account.service.EventLogServicePostEvent;
import account.service.UserService;
import account.dto.PasswordDTO;
import account.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@Validated
public class UserController {

    final UserService userService;
    final EventLogServicePostEvent eventLogService;

    public UserController(@Autowired UserService userService, EventLogServicePostEvent eventLogService) {
        this.userService = userService;
        this.eventLogService = eventLogService;
    }

    @PostMapping(Signup.PATH)
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO dto = UserDTO.convertUserToDTO(userService.signUp(userDTO.convertDTOToUser()));

        // EVENT_LOG: CREATE_USER
        eventLogService.postEvent(ActionType.CREATE_USER, "Anonymous", dto.getEmail(), Signup.PATH);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(ChangePass.PATH)
    public ResponseEntity<PasswordDTO> changePassword(@Valid @RequestBody PasswordDTO passwordDTO, @AuthenticationPrincipal UserDetails details) {

        userService.changePass(details.getUsername(), passwordDTO.getPassword());

        passwordDTO.setEmail(details.getUsername());
        passwordDTO.setStatus("The password has been updated successfully");

        // EVENT_LOG: CHANGE_PASSWORD
        eventLogService.postEvent(ActionType.CHANGE_PASSWORD, details.getUsername(), details.getUsername(), ChangePass.PATH);
        return new ResponseEntity<>(passwordDTO, HttpStatus.OK);
    }

    @GetMapping(User.PATH + "/")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsersData() {
        return userService.getAllUsers();
    }

    @DeleteMapping(User.PATH + "/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> deleteUser(@PathVariable String email, @AuthenticationPrincipal UserDetails details) {
        userService.deleteUser(email);

        // EVENT_LOG: DELETE_USER
        eventLogService.postEvent(ActionType.DELETE_USER, details.getUsername(), email, User.PATH);
        return Map.of("user", email, "status", "Deleted successfully!");
    }

    @PutMapping(Role.PATH)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO changeUserRoles(@Valid @RequestBody RoleOpsDTO roleOps, @AuthenticationPrincipal UserDetails details) {
        UserDTO userDTO = userService.changeRole(roleOps.getUser(), roleOps.getRole(), roleOps.getOperation());

        // EVENT_LOG: GRANT_ROLE or REMOVE_ROLE
        switch (roleOps.getOperation()) {
            case "GRANT" -> eventLogService.postEvent(ActionType.GRANT_ROLE, details.getUsername(), "Grant role " + roleOps.getRole().replace("ROLE_", "") + " to " + roleOps.getUser(), Role.PATH);
            case "REMOVE" -> eventLogService.postEvent(ActionType.REMOVE_ROLE, details.getUsername(), "Remove role " + roleOps.getRole().replace("ROLE_", "") + " from " + roleOps.getUser(), Role.PATH);
        }
        return userDTO;
    }

    @PutMapping(Access.PATH)
    @ResponseStatus(HttpStatus.OK)
    public StatusResponse changeUserAccess(@Valid @RequestBody AccessOpsDTO accessOpsDTO, @AuthenticationPrincipal UserDetails details) {
        userService.changeAccess(accessOpsDTO);

        // EVENT_LOG: LOCK_USER, UNLOCK_USER
        switch (accessOpsDTO.getOperation().toUpperCase()) {
            case "LOCK" -> {
                eventLogService.postEvent(ActionType.LOCK_USER, details.getUsername(), "Lock user " + accessOpsDTO.getUser(), Access.PATH);
            }
            case "UNLOCK" -> {
                eventLogService.postEvent(ActionType.UNLOCK_USER, details.getUsername(), "Unlock user " + accessOpsDTO.getUser(), Access.PATH);
            }
        }
        return new StatusResponse("User " + accessOpsDTO.getUser() + " " + accessOpsDTO.getOperation().toLowerCase() + "ed!");
    }
}
