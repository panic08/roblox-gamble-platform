package ru.marthastudios.robloxcasino.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtPhraseTokenUtil {
    @Value("${jwt.phraseSecret}")
    private String jwtPhraseSecret;

    public String generateToken(String robloxNickname, String phrases){
        Map<String, Object> claims = new HashMap<>();

        claims.put("phrases", phrases);

        return createToken(claims, robloxNickname);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtPhraseSecret.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String createToken(Map<String, Object> claims, String subject){
        Date dateNow = new Date();
        Date expirationDate = new Date(dateNow.getTime() + 10 * 60 * 1000);

        byte[] signingKeyBytes = jwtPhraseSecret.getBytes();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(dateNow)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(signingKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubjectFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractPhrasesFromToken(String token){
        return extractClaim(token, claims -> (String) claims.get("phrases"));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtPhraseSecret.getBytes()).build().parseClaimsJws(token).getBody();
    }
}
