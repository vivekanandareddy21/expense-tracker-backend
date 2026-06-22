package com.vivek.expense_tracker.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey12345";

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email) {

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60)
                )
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        System.out.println("GENERATED TOKEN = " + token);

        return token;
    }

    public String extractEmail(String token) {

        String email = extractAllClaims(token).getSubject();

        System.out.println("EXTRACTED EMAIL = " + email);

        return email;
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String email) {

        String extractedEmail = extractEmail(token);

        boolean valid = extractedEmail.equals(email)
                && !isTokenExpired(token);

        System.out.println("TOKEN VALID = " + valid);

        return valid;
    }

    private boolean isTokenExpired(String token) {

        boolean expired = extractAllClaims(token)
                .getExpiration()
                .before(new Date());

        System.out.println("TOKEN EXPIRED = " + expired);

        return expired;
    }
}