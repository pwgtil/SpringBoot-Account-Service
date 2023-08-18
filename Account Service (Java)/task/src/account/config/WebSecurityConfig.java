package account.config;

import account.authorization.UserRole;
import account.controller.routing.*;
import account.service.EventLogServicePostEvent;
import account.service.UserService;
import account.service.PasswordService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private static final String USER_ROLE = UserRole.ROLE_USER.name();
    private static final String ACCOUNTANT_ROLE = UserRole.ROLE_ACCOUNTANT.name();
    private static final String ADMINISTRATOR_ROLE = UserRole.ROLE_ADMINISTRATOR.name();
    private static final String AUDITOR_ROLE = UserRole.ROLE_AUDITOR.name();

    UserDetailsService userService;

    public void configure(@Autowired @NotNull AuthenticationManagerBuilder auth, @Autowired PasswordService passwordService) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordService.getPasswordEncoder());
//                .and()
//
//                .inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("ADMIN");
                //.and().passwordEncoder(NoOpPasswordEncoder.getInstance());

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Autowired EventLogServicePostEvent eventLogService) throws Exception {
        return http
                .httpBasic()
                .authenticationEntryPoint(getAuthenticationEntryPoint(eventLogService))
                .and()
                .csrf().ignoringRequestMatchers(toH2Console()).disable()
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, Signup.PATH)
                            .permitAll()
                        .requestMatchers(ChangePass.PATH)
                            .authenticated()
                        .requestMatchers(Payment.PATH)
                            .hasAnyAuthority(USER_ROLE, ACCOUNTANT_ROLE)
                        .requestMatchers(Payments.PATH)
                            .hasAuthority(ACCOUNTANT_ROLE)
                        .requestMatchers(User.PATH, User.PATH + "/*", Role.PATH)
                            .hasAuthority(ADMINISTRATOR_ROLE)
                        .requestMatchers(Access.PATH)
                            .hasAuthority(ADMINISTRATOR_ROLE)
                        .requestMatchers(Events.PATH, Events.PATH + "/*")
                            .hasAuthority(AUDITOR_ROLE)
                        .requestMatchers(toH2Console())
                            .permitAll()
                        .requestMatchers("/actuator/shutdown")
                            .permitAll()
                        .requestMatchers("/error")
                            .permitAll()
                        .anyRequest().denyAll())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(getCustomAccessDeniedHandler(eventLogService))
//                .accessDeniedPage("/accessDenied.jsp")
                .and()
                .build();
    }

    @Bean
    private static RestAuthenticationEntryPoint getAuthenticationEntryPoint(EventLogServicePostEvent eventLogService) {
        return new RestAuthenticationEntryPoint(eventLogService);
    }

    private CustomAccessDeniedHandler getCustomAccessDeniedHandler(EventLogServicePostEvent eventLogService) {
        return new CustomAccessDeniedHandler(eventLogService);
    }

    public WebSecurityConfig(@Autowired UserService userService) {
        this.userService = userService;
    }
}
