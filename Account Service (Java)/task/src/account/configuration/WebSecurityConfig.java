package account.configuration;

import account.businesslayer.UserService;
import account.presentation.routing.Payment;
import account.presentation.routing.Signup;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    UserDetailsService userService;

    public void configure(@Autowired @NotNull AuthenticationManagerBuilder auth, @Autowired PasswordEncoder encoder) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(encoder);
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN")
                .and().passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic()
//                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(Signup.PATH, "/h2-console", "/actuator/shutdown").permitAll();
                    auth.requestMatchers(Payment.PATH).authenticated();
                    auth.anyRequest().authenticated();
                })
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .formLogin(Customizer.withDefaults())
                .build();

        //        http.authorizeHttpRequests()
////                .requestMatchers("/admin").hasRole("ADMIN")
////                .requestMatchers("/user").hasAnyRole("ADMIN", "USER")
//                .requestMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic()
//                .and()
//                .formLogin(Customizer.withDefaults());
//        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    public WebSecurityConfig(@Autowired UserService userService) {
        this.userService = userService;
    }
}
