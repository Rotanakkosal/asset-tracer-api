package com.kshrd.asset_tracer_api.config;

import com.kshrd.asset_tracer_api.model.entity.UserApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtUtil {
    private static final String SECRET_KEY = "78214125442A472D4B6150645367566B59703373367639792F423F4528482B4D";
    private static final long JWT_TOKEN_VALIDITY = 1000 * 600 * 600;
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /***
     * generateToken as name of method that it use for generate token when user authenticated
     */
    public String generateToken(UserApp userApp){
        return generateToken(new HashMap<>(), userApp);
    }

    /***
     * generateToken that is the method overloading that use to generate token for user when
     * User has authenticate already
     * .setExpiration(new Date(System.currentTimeMillis() + 1000 *600 *24)) // It use for
     *  set time to our token to be expire during 24 day
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserApp userApp
    ){
        extraClaims.put("id", userApp.getId());
        extraClaims.put("name", userApp.getName());
        extraClaims.put("image", userApp.getImage());
        extraClaims.put("email", userApp.getEmail());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userApp.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 36 * 24)) // set to expire in 3 months
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // set to expire in 3 months
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
