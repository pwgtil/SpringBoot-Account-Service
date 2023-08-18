package account.config;

import account.entity.enums.ActionType;
import account.service.EventLogServicePostEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final EventLogServicePostEvent eventLogService;

    public RestAuthenticationEntryPoint(EventLogServicePostEvent eventLogServicePostEvent) {
        this.eventLogService = eventLogServicePostEvent;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());//"Access Denied!");
        // EVENT_LOG: LOGIN_FAILED
//        eventLogService.postEvent(ActionType.LOGIN_FAILED, request.getRemoteUser(), request.getServletPath(), request.getServletPath());
    }
}