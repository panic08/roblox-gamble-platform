package ru.marthastudios.robloxcasino.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.marthastudios.robloxcasino.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtAccessTokenUtil {
    @Value("${jwt.accessSecret}")
    private String jwtAccessSecret;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", user.getRole());
        claims.put("registered_at", user.getRegisteredAt());

        return createToken(claims, user.getId());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtAccessSecret.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String createToken(Map<String, Object> claims, long subject){
        Date dateNow = new Date();
        Date expirationDate = new Date(dateNow.getTime() + 1000 * 60 * 60 * 24 * 23);

        byte[] signingKeyBytes = jwtAccessSecret.getBytes();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(String.valueOf(subject))
                .setIssuedAt(dateNow)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(signingKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractSubjectFromToken(String token){
        return extractClaim(token, claims -> Long.parseLong(claims.getSubject()));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtAccessSecret.getBytes()).build().parseClaimsJws(token).getBody();
    }
}
