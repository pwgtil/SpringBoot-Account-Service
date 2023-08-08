package account.config;

import account.controller.routing.*;
import account.service.UserService;
import account.service.PasswordService;
import account.security.RestAuthenticationEntryPoint;
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

    UserDetailsService userService;

    public void configure(@Autowired @NotNull AuthenticationManagerBuilder auth, @Autowired PasswordService passwordService) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordService.getPasswordEncoder())
                .and()

                .inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN");
                //.and().passwordEncoder(NoOpPasswordEncoder.getInstance());

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .csrf().ignoringRequestMatchers(toH2Console()).disable()
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, Signup.PATH).permitAll()
                        .requestMatchers(ChangePass.PATH).authenticated()
                        .requestMatchers(Payment.PATH).hasAnyRole("USER", "ACCOUNTANT")
                        .requestMatchers(Payments.PATH).hasRole("ACCOUNTANT")
                        .requestMatchers(User.PATH, Role.PATH).hasRole("ADMINISTRATOR")
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied.jsp")
                .and()
                .build();
    }

    public WebSecurityConfig(@Autowired UserService userService) {
        this.userService = userService;
    }
}
