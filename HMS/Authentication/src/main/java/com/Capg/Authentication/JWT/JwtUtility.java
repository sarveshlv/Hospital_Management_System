package com.Capg.Authentication.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtility {

    @Value("${jwtSecret}")
    private String SECRET_KEY;

    @Value("${jwtExpirationMs}")
    private int EXPIRATION_IN_MS;

    public Boolean validateToken(final String token, String email) {
        String emailFromToken = getUsernameFromToken(token);
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return (emailFromToken.equals(email) && !isTokenExpired(token));
    }

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Collection<String> roles = authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        return createToken(claims, email);
    }

    private String createToken(Claims claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
