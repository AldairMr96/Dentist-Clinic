package com.mycompany.clinica_odontologica.config;

import com.mycompany.clinica_odontologica.config.filter.JwtTokenValidator;
import com.mycompany.clinica_odontologica.service.UserService;
import com.mycompany.clinica_odontologica.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

                    }
                    )
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

}
