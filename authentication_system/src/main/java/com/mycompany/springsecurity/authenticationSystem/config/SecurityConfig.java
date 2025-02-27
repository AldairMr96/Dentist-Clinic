package com.mycompany.springsecurity.authenticationSystem.config;

import com.mycompany.springsecurity.authenticationSystem.config.filter.JwtTokenValidator;
import com.mycompany.springsecurity.authenticationSystem.service.UserService;
import com.mycompany.springsecurity.authenticationSystem.util.JwtUtils;
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

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenValidator jwtTokenValidator;
    @Autowired
    private JwtUtils jwtUtils;


    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,JwtTokenValidator jwtTokenValidator) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtTokenValidator =jwtTokenValidator;
    }
    //configure the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable()) //cross site request forgering
                .httpBasic(Customizer.withDefaults()) //Defalt is user and password
                .sessionManagement(
                        session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {

                    http.requestMatchers(HttpMethod.GET, "/authentication/get").permitAll();

                    http.requestMatchers(HttpMethod.POST, "/authentication/log-in").permitAll();

                    http.requestMatchers(HttpMethod.POST, "/authentication/sign-in").permitAll();


                    http.requestMatchers(HttpMethod.GET, "/authentication/find").hasAnyRole("DEVELOPER", "MANAGER");


                    http.requestMatchers(HttpMethod.PUT,"/authentication/edit").hasAnyRole("DEVELOPER", "MANAGER");

                    http.requestMatchers(HttpMethod.DELETE,"/authentication/delete").hasAnyRole("DEVELOPER", "MANAGER");




                   http.anyRequest().authenticated();
                })

                .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager () throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserService userDetailsServiceImpl){
        DaoAuthenticationProvider dap =  new DaoAuthenticationProvider();
        dap.setPasswordEncoder(passwordEncoder());
        dap.setUserDetailsService(userDetailsServiceImpl);
        return dap ;
    }



}

