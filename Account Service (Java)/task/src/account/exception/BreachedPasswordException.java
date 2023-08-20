package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// just an example of custom exception. I'm handling it in a different way by custom handler
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The password is in the hacker's database!")
public class BreachedPasswordException extends RuntimeException {
}
