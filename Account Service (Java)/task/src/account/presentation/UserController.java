package account.presentation;

import account.businesslayer.User;
import account.businesslayer.UserService;
import account.presentation.routing.Payment;
import account.presentation.routing.Signup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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
    public ResponseEntity saveUser(@Valid @RequestBody UserDTO dtoInput) {
        if (!validateDTO(dtoInput)) {
            return new ResponseEntity("Error", HttpStatus.BAD_REQUEST);
        }
        dtoInput.setPassword(encoder.encode(dtoInput.getPassword()));
        UserDTO dto = UserDTO.convertUserToDTO(userService.save(dtoInput.convertDTOToUser()));
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new CustomErrorMessage(LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "User exist!",
                            Signup.PATH
                    ), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(Payment.PATH)
    public ResponseEntity payment(@AuthenticationPrincipal UserDetails details) {
        User user = (User) userService.loadUserByUsername(details.getUsername());
        return new ResponseEntity<>(UserDTO.convertUserToDTO(user), HttpStatus.OK);

    }

    private boolean validateDTO(UserDTO dto) {
        return dto.getEmail() != null
                && dto.getEmail().matches("[\\w.]+(@acme.com)")
                && dto.getName() != null
                && !dto.getName().isEmpty()
                && dto.getLastname() != null
                && !dto.getLastname().isEmpty()
                && dto.getPassword() != null
                && !dto.getPassword().isEmpty();
    }

}
