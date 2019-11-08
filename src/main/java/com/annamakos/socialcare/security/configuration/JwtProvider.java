package com.annamakos.socialcare.security.configuration;

import com.annamakos.socialcare.model.User;
import com.annamakos.socialcare.security.service.UserPrinciple;
import com.annamakos.socialcare.security.service.UsersDetailsService;
import io.jsonwebtoken.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("$jwt.secret.key")
    private String secret;
    @Value("$jwt.token.expiration")
    private String tokenExpiration;

    public String generateToken(Authentication authentication){
        UserPrinciple user = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3600000 ))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch(SignatureException e){
            logger.error("Invalid signature: {}", e.getMessage());
        } catch(MalformedJwtException e){
            logger.error("Invalid token: {}", e.getMessage());
        } catch(ExpiredJwtException e){
            logger.error("Expired token: {}", e.getMessage());
        } catch(UnsupportedJwtException e){
            logger.error("Unsupported token: {}", e.getMessage());
        } catch(IllegalArgumentException e){
            logger.error("Claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
