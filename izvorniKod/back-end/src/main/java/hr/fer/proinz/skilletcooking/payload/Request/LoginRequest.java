package hr.fer.proinz.skilletcooking.payload.Request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

//this annotation generates getters
@Getter

// this annotation generates setters
@Setter

/** A class that contains all the necessary data for a login */
public class LoginRequest {
    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** A attribute that contains the username to be authenticated */
    private String username;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** A attribute that contains the password to be authenticated */
    private String password;
}
