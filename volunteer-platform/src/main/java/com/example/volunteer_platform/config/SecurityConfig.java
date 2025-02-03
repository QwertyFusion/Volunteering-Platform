package com.example.volunteer_platform.config;

import com.example.volunteer_platform.security.CustomAuthenticationSuccessHandler;
import com.example.volunteer_platform.service.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserServiceImplementation userServiceImplementation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home", "/login", "/signup/**", "/api/**", "/register/**").permitAll() // Ensure these are permitted
                        .requestMatchers("/o/profile/**", "/v/profile/**").authenticated()
                        .requestMatchers("/o/**").hasRole("ORGANIZATION")
                        .requestMatchers("/v/**").hasRole("VOLUNTEER")
                        .requestMatchers(HttpMethod.DELETE, "/v/**").hasRole("VOLUNTEER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler) // Use the custom success handler
                        .failureUrl("/login?error=true") // Redirect to log in with error
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Specify the logout URL
                        .logoutSuccessUrl("/home") // Redirect to /home after logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServiceImplementation);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userServiceImplementation).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
    
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}