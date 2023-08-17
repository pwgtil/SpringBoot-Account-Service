package account.config;

import account.service.EventLogServicePostEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    final EventLogServicePostEvent eventLogServicePostEvent;

    @Autowired
    public AuthenticationFailureListener(EventLogServicePostEvent eventLogServicePostEvent) {
        this.eventLogServicePostEvent = eventLogServicePostEvent;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Object userName = event.getAuthentication().getPrincipal();
        Object credentials = event.getAuthentication().getCredentials();
        int test;
    }
}
