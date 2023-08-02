package account.controller;

import account.businesslayer.UserService;
import account.dto.PasswordDTO;
import account.dto.UserDTO;
import account.controller.routing.Signup;
import account.controller.routing.ChangePass;
import account.security.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Validated
public class UserController {

    final PasswordService passwordService;

    final UserService userService;

    public UserController(@Autowired UserService userService, @Autowired PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @PostMapping(Signup.PATH)
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO dtoInput) {

        if (userService.loadUserByUsername(dtoInput.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }

        // Check length
        if (!passwordService.hasValidLength(dtoInput.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password length must be at least 12 chars!");
        }
        // Check if in hacker database
        if (!passwordService.isSafe(dtoInput.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }

        dtoInput.setPassword(passwordService.getPasswordEncoder().encode(dtoInput.getPassword()));
        UserDTO dto = UserDTO.convertUserToDTO(userService.save(dtoInput.convertDTOToUser()));
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

//    @GetMapping(Payment.PATH)
//    public ResponseEntity<UserDTO> payment(@AuthenticationPrincipal UserDetails details) {
//        UserDTO userDTO = UserDTO.convertUserToDTO(userService.findUserByEmail(details.getUsername()));
//        return new ResponseEntity<>(userDTO, HttpStatus.OK);
//
//    }

    @PostMapping(ChangePass.PATH)
    public ResponseEntity<PasswordDTO> changePassword(@Valid @RequestBody PasswordDTO passwordDTO, @AuthenticationPrincipal UserDetails details) {
        // Check length
        if (!passwordService.hasValidLength(passwordDTO.getNewPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
        }
        // Check if in hacker database
        if (!passwordService.isSafe(passwordDTO.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }

        UserDTO userDTO = UserDTO.convertUserToDTO(userService.findUserByEmail(details.getUsername()));

        // Check duplication
        if (!passwordService.isNotPreviousPassword(passwordDTO.getNewPassword(), userDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        // All OK
        userDTO.setPassword(passwordService.getPasswordEncoder().encode(passwordDTO.getNewPassword()));
        userService.save(userDTO.convertDTOToUser());
        passwordDTO.setEmail(userDTO.getEmail());
        passwordDTO.setStatus("The password has been updated successfully");

        return new ResponseEntity<>(passwordDTO, HttpStatus.OK);
    }
}
