package com.example.learning_management.config;

import org.springframework.stereotype.Service;

import com.example.learning_management.token.TokenRepository;
import com.example.learning_management.token.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final TokenRepository tokenRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        var current = System.currentTimeMillis();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + expiration))
                .signWith(getSignkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private Claims extractAllClaims(String token) {
        // buil JwtParser with JwtParser then get Claims (DefaultJwtParserBuilder,
        // DefaultJwtBuilder)
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // use this function for(setSigninKey and signWith) instead of the raw String
    private Key getSignkey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails){
        return isTokenvalid(token, userDetails, TokenType.ACCESS_TOKEN);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails){
        return isTokenvalid(token, userDetails, TokenType.REFRESH_TOKEN);
    }

    private boolean isTokenvalid(String token, UserDetails userDetails, TokenType tokenType) {
        if(token == null){
            return false;
        }
        
        // check exist, tokenType, expired and revoked in database
        var tokenDB = tokenRepository.findByToken(token).orElse(null);
        if (tokenDB == null || tokenDB.isExpired() || tokenDB.isRevoked() && tokenDB.getTokenType() == tokenType) {
            return false;
        }

        //check user, secretKey, time expired
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
