package com.example.todo_list_api.config;

import com.example.todo_list_api.service.IUserService;
import com.example.todo_list_api.utils.CustomAccessDeniedHandler;
import com.example.todo_list_api.utils.CustomAuthenticationEntryPoint;
import com.example.todo_list_api.utils.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/logout", "/403").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/tasks/create", "/tasks/edit/**", "/tasks/update/**").hasRole("USER")
                .requestMatchers("/tasks/delete/**", "/userInfo", "/tasks").hasAnyRole("USER", "ADMIN")

                // --- API ---
                .requestMatchers(HttpMethod.POST,"/api/register", "/api/login").permitAll() // Cho phép không login
                .requestMatchers("/api/**").authenticated()                 // Các API còn lại: cần login
                .requestMatchers("/api/user-info").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/users").hasRole("ADMIN")
                .anyRequest().authenticated()
        );


        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/process-login")
                .defaultSuccessUrl("/userInfo", true)
                .failureHandler(failureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
        );

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationProvider provider)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }

}


