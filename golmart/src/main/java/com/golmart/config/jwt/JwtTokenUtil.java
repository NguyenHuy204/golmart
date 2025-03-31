package com.golmart.config.jwt;

import com.golmart.entity.UserDetailsImpl;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private static final Gson gson = new Gson();

    @Value("Private KEY")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * The function takes a userDetails object and returns a JWT token
     *
     * @param userDetails This is the user object that contains the user's information.
     * @return A JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("data", gson.toJson(userDetails));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public UserDetailsImpl decodeToken(String token) {
        return gson.fromJson((String) getAllClaimsFromToken(token).get("data"), UserDetailsImpl.class);
    }

    /**
     * It takes a map of claims, a subject, and a secret key, and returns a JWT token
     *
     * @param claims  A map of claims that will be added to the JWT.
     * @param subject The subject of the token.
     * @return A JWT token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 10000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    /**
     * If the username in the token matches the username in the userDetails object, and the token is not expired, then
     * return true
     *
     * @param token       The JWT token to validate
     * @param userDetails The user details object that contains the username and password of the user.
     * @return A boolean value.
     */
    public Boolean validateToken(String token, UserDetails userDetails, String username) {
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
