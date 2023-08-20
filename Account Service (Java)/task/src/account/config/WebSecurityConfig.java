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

    private final UserDetailsService userService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsService userService, RestAuthenticationEntryPoint restAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userService = userService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Autowired EventLogServicePostEvent eventLogService) throws Exception {
        return http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
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
                .accessDeniedHandler(customAccessDeniedHandler)
//                .accessDeniedPage("/accessDenied.jsp")
                .and()
                .build();
    }

    public void configure(@Autowired @NotNull AuthenticationManagerBuilder auth, @Autowired PasswordService passwordService) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordService.getPasswordEncoder());
//                .and()
//                .inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("ADMIN");
//                .and().passwordEncoder(NoOpPasswordEncoder.getInstance());

    }

}
