package com.adega.ms.user.adegauserservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService{

    @Value("${security.jwt.secret.key}")
    private String SECRET_KEY;

    private final String ISSUER = "AdegaRequinteIssuer";

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String generateToken(String email) {
        return "Bearer " + Jwts.builder()
                .setSubject(email)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*60*1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailByToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private @NotNull Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private boolean isNotExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .after(new Date(System.currentTimeMillis()));
    }

    private boolean isSameIssuer(String token) {
        return extractClaim(token, Claims::getIssuer).equals(ISSUER);
    }

    public boolean validateToken(String token) {
        return isSameIssuer(token) && isNotExpired(token);
    }

}
