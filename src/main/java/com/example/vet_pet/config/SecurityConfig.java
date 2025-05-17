package com.example.vet_pet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] SWAGGER_ENDPOINT = {
            "/**swagger**/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };


    @Order(1)
    @Configuration
    public class SwaggerConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("Admin")
                    .password(passwordEncoder().encode("Admin"))
                    .authorities("ROLE_USER");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .requestMatchers().antMatchers(SWAGGER_ENDPOINT)
                    .and()
                    .authorizeRequests().anyRequest().hasAuthority("ROLE_USER")
                    .and()
                    .httpBasic();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Order(2)
    @Configuration
    @RequiredArgsConstructor
    public static class RestConfiguration extends WebSecurityConfigurerAdapter {
        private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        @Bean
        @Override
        public AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .cors()
                    .and()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/doctors/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/timetable/**").permitAll()
                    .antMatchers("/api/login/**", "/api/token/refresh/**", "/api/register/**").permitAll()
                    .antMatchers(SWAGGER_ENDPOINT).permitAll()
                    .antMatchers(POST, "/appointment/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(DELETE, "/appointment/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(POST, "/doctors/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(DELETE, "/doctors/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(DELETE, "/pet/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(POST, "/timetable/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(PUT, "/timetable/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(DELETE, "/timetable/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(POST, "/users/**").hasAnyAuthority("ROLE_ADMIN")
                    .antMatchers(DELETE, "/users/**").hasAnyAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated();
        }

    }
}
