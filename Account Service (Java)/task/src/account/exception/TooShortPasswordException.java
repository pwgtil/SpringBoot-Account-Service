package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The password length must be at least 12 chars!")
public class TooShortPasswordException extends RuntimeException {
}
