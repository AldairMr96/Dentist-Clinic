package com.mycompany.clinica_odontologica.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    @Value("${security.jwt.key}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public  String createToken(Authentication authentication){
        //Algoritm is Sha 256
        Algorithm algorithmCrypted = Algorithm.HMAC256(this.privateKey);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 300000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithmCrypted);

        return jwtToken;

    }
    public DecodedJWT verifyToken (String jwtToken){
        try {
            Algorithm algorithmCrypted =Algorithm.HMAC256(this.privateKey);
            JWTVerifier jwtVerifier =JWT.require(algorithmCrypted)
                    .withIssuer(this.userGenerator)
                    .build();
            DecodedJWT decodedJWT =jwtVerifier.verify(jwtToken);
            return decodedJWT;

        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Token invalid, not authorized");
        }
    }
    public String extractUsername(DecodedJWT decodedJWT) {


        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String clameName) {

        return decodedJWT.getClaim(clameName);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT) {

        return decodedJWT.getClaims();
    }
}
