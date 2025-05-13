package com.mycompany.clinica_odontologica.util.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mycompany.clinica_odontologica.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class jwtUtilTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtUtils jwtUtils;
    private final String privateKey = "my_secret_key"; // Simulates the JWT secret key
    private final String username = "testUser";
    private final String authorities = "ROLE_SECRETARIAT";
    private String token ;
    private JwtUtils jwtUtilsSpy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtils, "privateKey", privateKey);

        // Genered test token
        token = jwtUtils.createToken(username, authorities);
        jwtUtilsSpy = Mockito.spy(jwtUtils);
    }

    @Test
    void testCreateTokenSuccess() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsernameSuccess() {
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testVerifyToken_ValidToken() {

        String username = "test_user";
        String expectedUsername = "test_user";
        doReturn(expectedUsername).when(jwtUtilsSpy).extractUsername(token);
        doReturn(false).when(jwtUtilsSpy).isTokenExpired(token);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getUsername()).thenReturn(expectedUsername);

        String validToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000)) // valided 1 minute
                .sign(Algorithm.HMAC256(privateKey));

        // Act & Assert
        assertTrue(jwtUtils.verifyToken(validToken, userDetails));
        assertTrue(username.equals(userDetails.getUsername()));
        assertFalse(jwtUtils.isTokenExpired(token));
    }
    @Test
    void testVerifyToken_ExpiredToken() {
        // Arrange: Create Expired Token

        String expiredToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() - 1000)) //expired
                .sign(Algorithm.HMAC256(privateKey));

        // Act & Assert
        assertFalse(jwtUtils.verifyToken(expiredToken, userDetails));
    }

    @Test
    void testVerifyToken_InvalidSignature() {
        // Arrange: Crear un token con firma incorrecta
        String invalidToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256("wrong_secret")); // ❌ Firma incorrecta

        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtils.verifyToken(invalidToken, userDetails));
    }

    @Test
    void testVerifyToken_MalformedToken() {
        // Arrange: Token mal formado
        String malformedToken = "invalid.token.string";

        // Act & Assert
        assertThrows(JWTDecodeException.class, () -> jwtUtils.verifyToken(malformedToken, userDetails));
    }

    @Test
    void testVerifyToken_InvalidUser() {
        // Arrange: Token con un usuario diferente
        String tokenWithWrongUser = JWT.create()
                .withSubject("wrong_user") // Usuario incorrecto
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(privateKey));

        // Act & Assert
        assertFalse(jwtUtils.verifyToken(tokenWithWrongUser, userDetails));
    }

    @Test
    void testIsTokenExpiredFalse() {
        assertFalse(jwtUtils.isTokenExpired(token));
    }

    @Test
    void testExtractAuthoritiesSuccess() {
        String extractedAuthorities = jwtUtils.extractAuthorities(token);
        assertEquals(authorities, extractedAuthorities);
    }

    @Test
    void testInvalidTokenThrowsException() {
        String invalidToken = token + "invalid";

        assertThrows(JWTVerificationException.class, () -> {
            jwtUtils.extractUsername(invalidToken);
        });
    }




}
