package net.rogerplayer.csso.security;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.rogerplayer.csso.domain.CSSOUser;
import net.rogerplayer.csso.dto.CSSOUserDTO;

@Component
public class JwtTokenUtil implements Serializable{
	
private static final long serialVersionUID = 1L;
    
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String SIGNING_KEY = "chave@2020";
    public static final String TOKEN_PREFIX = "Roger ";
    public static final String HEADER_STRING = "Authorization";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(CSSOUser cssoUser) {
        return doGenerateToken( cssoUser);
    }

    private String doGenerateToken(CSSOUser  cssoUser) {
    	CSSOUserDTO cssoUserDTO = CSSOUserDTO.builder().user(cssoUser.getUser()).password(cssoUser.getPassword())
                .build();
      
        Claims claims = Jwts.claims().setSubject( cssoUser.getUser());
        claims.put("scopes", cssoUserDTO);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("Rogerplayer")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
              username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));
    }


}