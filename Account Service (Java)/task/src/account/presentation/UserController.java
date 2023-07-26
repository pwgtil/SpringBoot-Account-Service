package account.presentation;

import account.businesslayer.User;
import account.businesslayer.UserService;
import account.presentation.routing.Payment;
import account.presentation.routing.Signup;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Validated
public class UserController {

    final PasswordEncoder encoder;

    final UserService userService;

    public UserController(@Autowired UserService userService, @Autowired PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping(Signup.PATH)
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO dtoInput) {

        if (userService.loadUserByUsername(dtoInput.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists!");
        }

        dtoInput.setPassword(encoder.encode(dtoInput.getPassword()));
        UserDTO dto = UserDTO.convertUserToDTO(userService.save(dtoInput.convertDTOToUser()));
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @GetMapping(Payment.PATH)
    public ResponseEntity<UserDTO> payment(@AuthenticationPrincipal UserDetails details) {
        User user = (User) userService.loadUserByUsername(details.getUsername());
        return new ResponseEntity<>(UserDTO.convertUserToDTO(user), HttpStatus.OK);

    }

}
