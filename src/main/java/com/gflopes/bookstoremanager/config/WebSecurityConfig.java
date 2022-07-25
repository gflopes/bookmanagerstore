package com.gflopes.bookstoremanager.config;

import com.gflopes.bookstoremanager.enums.Role;
import com.gflopes.bookstoremanager.jwt.filter.JwtRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERS_API_URL = "/api/v1/users/**";
    private static final String PUBLISHERS_API_URL = "/api/v1/publishers/**";
    private static final String AUTHORS_API_URL = "/api/v1/authors/**";
    private static final String BOOKS_API_URL = "/api/v1/books/**";
    private static final String H2_CONSOLE_URL = "/h2-console/**";
    private static final String SWAGGER_URL = "/swagger-ui/index.html";

    private static final String ROLE_ADMIN = Role.ADMIN.getDescription();
    private static final String ROLE_USER = Role.USER.getDescription();

    private static final String[] SWAGGER_RESOURCES = {
            // -- swagger
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final UserDetailsService userDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers(USERS_API_URL, H2_CONSOLE_URL, SWAGGER_URL).permitAll()
                .antMatchers(PUBLISHERS_API_URL, AUTHORS_API_URL).hasAnyRole(ROLE_ADMIN)
                .antMatchers(BOOKS_API_URL).hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.headers().frameOptions().disable();

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SWAGGER_RESOURCES);
    }
}
