package com.hegoo.disgo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenUtils {

    private static final Long EXPIRE = 600000L;
    private static final String KEY = "KEYYYYYYYYYYYYYYYYYYYYYYYYYYY!";

    public static String generateToken(String userName) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * EXPIRE);
        return Jwts.builder().setHeaderParam("type", "JWT").setSubject(userName).setIssuedAt(now).setExpiration(expiration).signWith(SignatureAlgorithm.HS512, KEY).compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

}
