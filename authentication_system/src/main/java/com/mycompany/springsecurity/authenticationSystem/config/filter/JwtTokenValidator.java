package com.mycompany.springsecurity.authenticationSystem.config.filter;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.springsecurity.authenticationSystem.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
//Validated Token for each request
@Component
public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    public JwtTokenValidator (JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwtToken != null){

            //Delete bearer word
            jwtToken = jwtToken.substring(7);
           DecodedJWT decodedJWT= jwtUtils.verifyToken(jwtToken);


            String username = jwtUtils.extractUsername(decodedJWT);
            String stringAuthorities =  jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }
        filterChain.doFilter(request, response);
    }
}
