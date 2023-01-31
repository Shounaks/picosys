package com.example.picosys.config;

import com.example.picosys.entity.Role;
import com.example.picosys.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                //Allow this patterns for all
                .antMatchers("/swagger-resources/**","/v2/**","/v3/**", "/swagger-ui/**")
                .permitAll()
                .antMatchers("/api/v1/picosys/authentication/**")
                .permitAll()
                //Everything below this will be authenticated
                .antMatchers("/api/v1/picosys/planner/**")
                .hasAnyRole(Role.ADMIN.name(),Role.COMPENSATION_PLAN_USER.name())
                .antMatchers("/api/v1/picosys/admin/**")
                .hasRole(Role.ADMIN.name())
//                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
