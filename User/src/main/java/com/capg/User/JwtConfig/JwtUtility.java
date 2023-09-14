package com.capg.User.JwtConfig;

import com.capg.User.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtility {

    private String jwtSecret = "Sarvesh_Key";
    private int jwtValidity = 3600000;

    public String generateToken(Authentication authentication) {

        String username=((User)authentication.getPrincipal()).getUsername();//authentication.getName();
        Date now=new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ jwtValidity))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        parse(token);
        return true;
    }

    public String getUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    public Jws<Claims> parse(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    }
}
