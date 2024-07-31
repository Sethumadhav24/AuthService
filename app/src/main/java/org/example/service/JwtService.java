package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    public static final String SECRET = "4cda433c356b40b99ae895dd998d646401d4d7c3b9d30f5713cde17a2ed2fb79473302c00a87bc2b405c7a9377a8cba342dd1e9294da572f71ff29d51747f0769883d603a73435bb0e3302e5353ae166214da47a58db517c0a9c1aea7ceb42580b57a48086fd420841b5561e5c0da7238ae3f955ec5da9cc800b058b6dd8f7ff7942aaacee3638c63a40d380aebada50944ef26a8de78e10947753c04092516089245ea3ec5cc73c4e181a8c85104bf4161ce719aa977c4e0d7fadc284954a2155f884646351eb7ccbb2d4233d8048b8bad3791183403729d98aa1c3302351f79e45b7a82f52b83f1dfd7788aabc84c923ced4c29c8b710ddc16b1276b5be1ee";

    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
