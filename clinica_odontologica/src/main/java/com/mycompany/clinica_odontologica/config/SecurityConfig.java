package com.mycompany.clinica_odontologica.config;

import com.mycompany.clinica_odontologica.config.filter.JwtTokenValidator;
import com.mycompany.clinica_odontologica.service.UserService;
import com.mycompany.clinica_odontologica.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final AuthenticationConfiguration authConfiguration;
        private final JwtTokenValidator jwtTokenValidator;

        @Autowired
        private JWTUtils jwtUtils;

        public SecurityConfig(AuthenticationConfiguration authConfiguration, JwtTokenValidator jwtTokenValidator) {
            this.authConfiguration = authConfiguration;
            this.jwtTokenValidator = jwtTokenValidator;
        }


        @Bean
        public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws  Exception{
            return httpSecurity
                .csrf(csrf -> csrf.disable() )
                .httpBasic(Customizer.withDefaults())
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(http ->{
                    //Authentication dentist
                     http.requestMatchers(HttpMethod.GET, "/dental_clinic/dentist/get").permitAll();
                     http.requestMatchers(HttpMethod.GET, "/dental_clinic/dentist/find").hasAnyRole("DENTIST", "SECRETARIAT","PATIENT" );
                     http.requestMatchers(HttpMethod.POST, "/dental_clinic/dentist/create").hasAnyRole("DENTIST", "SECRETARIAT" );
                     http.requestMatchers(HttpMethod.PUT, "/dental_clinic/dentist/update").hasAnyRole("DENTIST", "SECRETARIAT" );
                     http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/dentist/delete").hasAnyRole("DENTIST", "SECRETARIAT" );
                    //Authentication patient
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/patient/get").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/patient/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    http.requestMatchers(HttpMethod.POST, "/dental_clinic/patient/create").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.PUT, "/dental_clinic/patient/update").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/patient/delete").hasAnyRole("DENTIST", "SECRETARIAT" );
                    //Authentication Responsible
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/responsible/get").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/responsible/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    http.requestMatchers(HttpMethod.POST, "/dental_clinic/responsible/create").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.PUT, "/dental_clinic/responsible/update").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/responsible/delete").hasAnyRole("DENTIST", "SECRETARIAT" );
                    //Authentication Schedule
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/schedule/get").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/schedule/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    http.requestMatchers(HttpMethod.POST, "/dental_clinic/schedule/create").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.PUT, "/dental_clinic/schedule/update").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/schedule/delete").hasAnyRole("DENTIST", "SECRETARIAT" );
                    //Authentication Secretariat
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/secretariat/get").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/secretariat/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    http.requestMatchers(HttpMethod.POST, "/dental_clinic/secretariat/create").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.PUT, "/dental_clinic/secretariat/update").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/secretariat/delete").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/secretariat/by-date").hasAnyRole("DENTIST", "SECRETARIAT" );

                    //Authentication Turn
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/turn/get").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/turn/find").hasAnyRole("DENTIST", "SECRETARIAT");
                    http.requestMatchers(HttpMethod.POST, "/dental_clinic/turn/create").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.PUT, "/dental_clinic/turn/update").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/turn/delete").hasAnyRole("DENTIST", "SECRETARIAT" );
                    http.requestMatchers(HttpMethod.GET, "/dental_clinic/turn/get-turns-for-day").hasAnyRole("DENTIST", "SECRETARIAT" );

                    //Authentication User
                     http.requestMatchers( HttpMethod.POST, "/dental_clinic/auth/login" ).permitAll();
                     http.requestMatchers(HttpMethod.POST, "/dental_clinic/auth/sigin").permitAll();
                     http.requestMatchers(HttpMethod.GET, "/dental_clinic/auth/get").hasAnyRole("DENTIST", "SECRETARIAT");
                     http.requestMatchers(HttpMethod.GET, "/dental_clinic/auth/find").hasAnyRole("DENTIST", "SECRETARIAT");
                     http.requestMatchers(HttpMethod.PUT, "/dental_clinic/auth/edit").hasAnyRole("DENTIST", "SECRETARIAT", "PATIENT");
                     http.requestMatchers(HttpMethod.DELETE, "/dental_clinic/auth/delete").hasAnyRole("DENTIST", "SECRETARIAT");

                http.anyRequest().authenticated();

                    })
           .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class)
            .build();

        }

    @Bean
    public AuthenticationManager authenticationManager () throws  Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (UserService userDeteailServiceImpl){
        DaoAuthenticationProvider dap =new DaoAuthenticationProvider();
        dap.setPasswordEncoder(passwordEncoder());
        dap.setUserDetailsService(userDeteailServiceImpl);
        return dap;

    }

     /* public static void main(String[] args) {
        // Instancia del encoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Contraseña en texto plano
        String rawPassword = "password";

        // Encriptar la contraseña
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // Mostrar la contraseña encriptada
        System.out.println("Contraseña encriptada: " + encryptedPassword);

    }*/

}
