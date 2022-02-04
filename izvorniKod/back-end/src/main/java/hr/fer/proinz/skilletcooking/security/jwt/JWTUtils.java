package hr.fer.proinz.skilletcooking.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import hr.fer.proinz.skilletcooking.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

//this annotation says that this class will be a bean
@Component

/** A class that provides methods for handling JWT tokens */
public class JWTUtils {

    // this annotation sets the value of the attribute to the value of the given
    // paramateer from the appplication.properties file
    @Value("${progimeri.app.jwtsecret}")

    /** A attribute that contains the key for hashing the JWT token */
    private String jwtSecretKey;

    @Value("${progimeri.app.jwtExpirationMs}")
    /** A attribute that contains the expiration time of a token */
    private String jwtExpiration;

    /**
     * A method that generates a token from the given authentication request
     *
     * @param authentication the authentication request of a user
     *
     * @return the jwt for the user from the authentication parameter
     */
    public String generateJWTToken(Authentication authentication) {
        // get the user details from the authentication request
        UserDetailsImpl userPricipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()// create a JWT token builder
                .setSubject(userPricipal.getUsername())// the key of the token will be the username
                .setIssuedAt(new Date())// set the creation date
                .setExpiration(new Date(new Date().getTime() + Long.parseLong(this.jwtExpiration)))// set the expiration
                // date
                .signWith(SignatureAlgorithm.HS512, this.jwtSecretKey)// hash the token
                .compact();// build the jwt and serialize it to a compact url safe string
    }

    /**
     * A method that returns the username from the given JWT token
     *
     * @param token the token from which the username has to be extracted
     * @return the username from the token
     */
    public String getUsernameFromJWTToken(String token) {
        return Jwts.parser()// create a jwt parser
                .setSigningKey(this.jwtSecretKey)// dehash the token
                .parseClaimsJws(token)// create a JWT object
                .getBody().getSubject();// get the subject
    }

    /**
     * A method that validates a given JWT token
     *
     * @param token the token to be validated
     * @return true if valid and false otherwise
     */
    public boolean validateJWTToken(String token) {
        try {
            Jwts.parser()// create a JWT parser
                    .setSigningKey(this.jwtSecretKey)// dehash the token
                    .parseClaimsJws(token);// create a object

            // if everything succeded return true
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: {" + e.getMessage() + "}");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {" + e.getMessage() + "}");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {" + e.getMessage() + "}");
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {" + e.getMessage() + "}");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {" + e.getMessage() + "}");
        }

        // if a exception occured return false
        return false;
    }
}
