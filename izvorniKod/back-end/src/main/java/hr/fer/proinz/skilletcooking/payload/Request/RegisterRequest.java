package hr.fer.proinz.skilletcooking.payload.Request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** An attribute that contains the username to be registered */
    private String username;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** An attribute that contains the firstName to be registered */
    private String firstName;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** An attribute that contains the lastName to be registered */
    private String lastName;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** An attribute that contains the password to be registered */
    private String password;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** An attribute that contains the email to be registered */
    private String email;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank
    /** An attribute that contains the dateOfBirth to be registered */
    private String dateOfBirth;
}
