package com.mycompany.clinica_odontologica.config;

import com.mycompany.clinica_odontologica.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) //Allow use of frames for H2 Console
                .httpBasic(Customizer.withDefaults()) // Default is user and password
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Authentication dentist
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/dentist/get").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/dentist/find").hasAnyRole("DENTIST", "SECRETARIAT", "PATIENT");
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/dentist/create").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/dentist/update").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/dentist/delete").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Authentication patient
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/patient/get").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/patient/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/patient/create").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/patient/update").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/patient/delete").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Authentication Responsible
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/responsible/get").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/responsible/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/responsible/create").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/responsible/update").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/responsible/delete").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Authentication Schedule
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/schedule/get").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/schedule/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/schedule/create").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/schedule/update").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/schedule/delete").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Authentication Secretariat
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/secretariat/get").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/secretariat/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/secretariat/create").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/secretariat/update").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/secretariat/delete").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/secretariat/by-date").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Authentication Turn
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/turn/get").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/turn/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/turn/create").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/turn/update").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/turn/delete").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/turn/get-turns-for-day").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Authentication User
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/auth/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/dental_clinic/auth/sigin").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/auth/get").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.GET, "/dental_clinic/auth/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    auth.requestMatchers(HttpMethod.PUT, "/dental_clinic/auth/edit").hasAnyRole("DENTIST", "SECRETARIAT", "PATIENT");
                    auth.requestMatchers(HttpMethod.DELETE, "/dental_clinic/auth/delete").hasAnyRole("DENTIST", "SECRETARIAT");

                    // Database H2
                    auth.requestMatchers( "/h2-console/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 🔹 Usa BCrypt para encriptar las contraseñas
    }
}
