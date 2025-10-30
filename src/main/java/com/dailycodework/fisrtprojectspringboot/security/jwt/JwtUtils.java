package com.dailycodework.fisrtprojectspringboot.security.jwt;


import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.dailycodework.fisrtprojectspringboot.security.user.ShopUserDetails;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private  String jwtSecret;//

    @Value("${auth.token.expirationInMils}")
    private int exparationTime;// 
    public String generateTokenForUser(Authentication authentication) {//
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();//

        List<String> roles =  userPrincipal.getAuthorities()//
            .stream()  //
            .map(GrantedAuthority::getAuthority).toList();//

           /*  return  Jwts.builder()//
                    .setSubject(userPrincipal.getEmail())//
                    .claim("id", userPrincipal.getId())//
                    .claim("roles", roles)//
                    .setIssuedAt(new Date())//
                    .setExpiration(new Date((new Date()) .getTime() + exparationTime))//
                    .signWith(key(),SignatureAlgorithm.HS256).compact();// */
            return Jwts.builder()
                .subject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + exparationTime))
                .signWith(key())
                .compact();

    }

    //Une version anterieure de la méthode generateTokenForUser(Authentication authentication) a cause de la version "io.jsonwebtoken"
  /*   private Key key(){
        return  Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));//
    } */

  /*   public String getUserNameFromToken(String token){
        return Jwts.parserBuilder()
            .setSignngKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
     */

     // Une version recente de la methode generateTokenForUser(Authentication authentication) a cause de la version "io.jsonwebtoken"
    private  SecretKey key(){//
        byte[] KeyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    public String getUserNameFromToken(String token){
        return Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token);
            return true;
        } catch (JwtException| IllegalArgumentException e) {
            throw new JwtException(e.getMessage());// Renvoyer une erreur si le token est invalide
        }
    }
}
