package com.minorProject.carShowcase.config;

import com.minorProject.carShowcase.security.JwtAuthFilter;
import com.minorProject.carShowcase.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // REGISTER authentication provider
//                .authenticationProvider(authenticationProvider())
//
//                // REGISTER authentication manager (IMPORTANT!)
//                .authenticationManager(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)))
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/showrooms").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/cars/showroom/{showroomId}").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/cars/public/{id}").permitAll()
//                        .requestMatchers("/book", "/prebook").permitAll()
//
//                        // OWNER ROLE PROTECTED ENDPOINTS
//                        .requestMatchers(HttpMethod.GET, "/api/cars/owner").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/cars").hasAuthority()()("ROLE_OWNER")
//                        .requestMatchers(HttpMethod.DELETE, "/api/cars/{id}").hasAuthority()()("ROLE_OWNER")
//                        .requestMatchers(HttpMethod.PUT, "/api/cars/{id}").hasAuthority()()("ROLE_OWNER")
//
//                        .anyRequest().authenticated()
//                )
//
//                // JWT Filter added BEFORE UsernamePasswordAuthenticationFilter
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/cars/owner").permitAll()
                        .requestMatchers(HttpMethod.POST, "/owner").hasAuthority("ROLE_OWNER")
                        .requestMatchers(HttpMethod.GET, "/owner").hasAuthority("ROLE_OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/owner").hasAuthority("ROLE_OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/auth/find/**").hasAuthority("ROLE_OWNER")
//                        .requestMatchers(HttpMethod.GET, "/showrooms/**","/showroom/**").hasAuthority("ROLE_OWNER")
                        .requestMatchers(HttpMethod.GET, "/showrooms/**","/showroom/**").permitAll()
                        .requestMatchers("/api/owner/cars").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .build();
    }

}
