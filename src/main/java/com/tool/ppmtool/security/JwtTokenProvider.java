package com.tool.ppmtool.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tool.ppmtool.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	/*
	 Additional Info:
	 As you noticed, the authenticationManager is the one that authenticates based on the correct username and password.  
	 Then the authenticationManager gives the "OK" for our tokenProvider to generate the JWT. That is the token that then accompanies  
	 each user's request to a secured end point and grants access.

	Remember, this is a Stateless API that only "opens its doors" if the request comes with a valid token. 
	A valid token can only be generated if the username and password are valid.
	 */
	
	/*
	 Generate the Token
	*/
	public String generateToken(Authentication authentication) {
		User user = (User)authentication.getPrincipal();
		 Date now = new Date(System.currentTimeMillis());
         Date expiryDate = new Date(now.getTime()+SecurityConstants.EXPIRATION_TIME);
         
         String userId = Long.toString(user.getId());
         
         //information we want to add to the token
         Map<String, Object> claims = new HashMap<>();
         claims.put("id", (Long.toString(user.getId())));
         claims.put("username", user.getUsername());
         claims.put("fullName", user.getFullName());
         
         // The token is a string
         return Jwts.builder()
                 .setSubject(userId)
                 .setClaims(claims)
                 .setIssuedAt(now)
                 .setExpiration(expiryDate)
                 .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                 .compact();
         }
	
	/*
	Validate the Token
	*/
	public boolean validateToken(String token) {
		try{
            Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            System.out.println("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }
	
	/*
	Get the user ID from the Token with
	 */
	 public Long getUserIdFromJWT(String token){
         Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
         String id = (String)claims.get("id");

         return Long.parseLong(id);
     }
}
