package account.security;

import account.businesslayer.UserService;
import account.presentation.routing.ChangePass;
import account.presentation.routing.Payment;
import account.presentation.routing.Payments;
import account.presentation.routing.Signup;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                        .requestMatchers(Payments.PATH).permitAll()
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .requestMatchers(Payment.PATH, ChangePass.PATH).authenticated()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

    public WebSecurityConfig(@Autowired UserService userService) {
        this.userService = userService;
    }
}
