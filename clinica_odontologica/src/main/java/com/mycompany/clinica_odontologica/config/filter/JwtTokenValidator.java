package com.mycompany.clinica_odontologica.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.clinica_odontologica.config.SecurityConfig;
import com.mycompany.clinica_odontologica.utils.JWTUtils;
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

@Component
public class JwtTokenValidator  extends OncePerRequestFilter {
private JWTUtils jwtUtils;

    public JwtTokenValidator(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {


        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken != null) {
            //Delete Bearer word
            jwtToken.substring(7);
            DecodedJWT decodedJWT = jwtUtils.verifyToken(jwtToken);

            String username = jwtUtils.extractUsername(decodedJWT);
            String stringAuth = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuth);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            filterChain.doFilter(request, response);

        }
    }

}
