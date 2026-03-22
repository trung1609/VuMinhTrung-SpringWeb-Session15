package com.example.session15.security.jwt;

import com.example.session15.entity.Users;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final UserRepository userRepository;
    @Value("${jwt_secret}")
    private String secretKey;

    @Value("${jwt_expire}")
    private long expire;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // accessToken
    public String generateAccessToken(String email) throws ResourceNotFoundException {
        Date now = new Date();
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expire))
                .signWith(key(), SignatureAlgorithm.HS512)
                .claim("role", users.getRole().name())
                .compact();
    }

    // refreshToken
    public String generateRefreshToken(String email) throws ResourceNotFoundException {
        Date now = new Date();
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expire * 7))
                .signWith(key(), SignatureAlgorithm.HS512)
                .claim("role", users.getRole().name())
                .compact();
    }

    // validateToken
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", "IllegalArgumentException");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", "UnsupportedJwtException");
        }
        return false;
    }

    // getUsernameFromToken
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
