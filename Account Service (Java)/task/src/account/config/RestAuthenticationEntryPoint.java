package account.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());//"Access Denied!");

        /*
        * I'll be testing here few things not necessarily needed for the project
        * */
        String requestURI = request.getRequestURI();
        String email = getUsernameFromAuthHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

    }

    private String getUsernameFromAuthHeader(String authHeader) {
        if (authHeader == null) {
            return "Anonymous";
        }
        String pair = new String(
                Base64.decodeBase64(authHeader.substring("Basic".length()))
        );
        return pair.split(":")[0];
    }
}