package com.chat.backend.utils;

import com.chat.backend.entities.ChatAppUser;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Log
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.severe(String.format("Invalid JWT signature - %s", ex.getMessage()));
        } catch (MalformedJwtException ex) {
            log.severe(String.format("Invalid JWT token - %s", ex.getMessage()));
        } catch (ExpiredJwtException ex) {
            log.severe(String.format("Expired JWT token - %s", ex.getMessage()));
        } catch (UnsupportedJwtException ex) {
            log.severe(String.format("Unsupported JWT token - %s", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            log.severe(String.format("JWT claims string is empty - %s", ex.getMessage()));
        }
        return false;
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[0];
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[1];
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public String generateAccessToken(ChatAppUser user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getUsername()))
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * @param request from controller methods
     * @return id - Get authorization header and validate
     */
    public String getUserId(HttpServletRequest request){
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.split(" ")[1].trim();
        return getUserId(token);
    }
}
