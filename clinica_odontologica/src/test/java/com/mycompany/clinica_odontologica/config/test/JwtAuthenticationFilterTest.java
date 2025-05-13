package com.mycompany.clinica_odontologica.config.test;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mycompany.clinica_odontologica.config.JwtAuthenticationFilter;
import com.mycompany.clinica_odontologica.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;



    @BeforeEach
    void setUp() {
        openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldProceedWithoutToken() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtils);
    }

    @Test
    void shouldAuthenticateWithValidToken() throws ServletException, IOException {
        String token = "valid.jwt.token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtUtils.extractUsername(token)).thenReturn("user");
        when(jwtUtils.extractAuthorities(token)).thenReturn("ROLE_USER");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(jwtUtils).extractUsername(token);
        verify(jwtUtils).extractAuthorities(token);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldRejectInvalidToken() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtUtils.extractUsername(token)).thenThrow(new JWTVerificationException("Invalid token"));
        PrintWriter printWriter = new PrintWriter("Invalid token");
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).getWriter();
        verifyNoMoreInteractions(filterChain);
    }
}
