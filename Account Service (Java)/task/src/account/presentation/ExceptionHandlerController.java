package account.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", HttpStatus.valueOf(status.value()).getReasonPhrase());
        body.put("message", "Error");
        body.put("path", request.getDescription(false).substring(4));
        LOGGER.info(ex.getMessage() + ex.getLocalizedMessage());

        return new ResponseEntity<>(body, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body2, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        var e = (ResponseStatusException) ex;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", HttpStatus.valueOf(status.value()).getReasonPhrase());
        body.put("message", e.getReason());
        body.put("path", request.getDescription(false).substring(4));
        LOGGER.info(headers.toString());

        return new ResponseEntity<>(body, status);
    }

//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<CustomErrorMessage> handleSignupUserExist(UserAlreadyExistsException e, WebRequest request) {
//        CustomErrorMessage body = new CustomErrorMessage(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Bad Request",
//                e.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
}
