package account.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied!");
//        response.addHeader("WWW-Authenticate", "Basic realm");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        final PrintWriter writer = response.getWriter();
//        writer.println("HTTP Status " + HttpServletResponse.SC_UNAUTHORIZED + " - " + authException.getMessage());
    }
}