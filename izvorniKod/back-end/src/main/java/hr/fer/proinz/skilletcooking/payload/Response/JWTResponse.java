package hr.fer.proinz.skilletcooking.payload.Response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

//this annotation generates getters
@Getter
// this annotation generates setters
@Setter
/** A class that represents a jwt that will be sent in a HTTP response */
public class JWTResponse {
    /** An attribtute that contains the JWT token */
    private String acessToken;

    /** An attribute that holds the token type which is Bearer */
    private String type = "Bearer";

    /** An attribute that represents the id of the user */
    private Integer id;

    /** An attribute that represents the username of the user */
    private String username;

    /** An attribute that represents the email of the user */
    private String email;

    /** An attribute that represents the roles of the user */
    private List<String> roles;

    /**
     * A constructor that generates a JWT repsonse from the given parameters
     *
     * @param accessToken the JWT token
     * @param id          the user id
     * @param username    the users username
     * @param email       the users email
     * @param roles       the users roles
     */
    public JWTResponse(String accessToken, Integer id, String username, String email, List<String> roles) {
        this.acessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
