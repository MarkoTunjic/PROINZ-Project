package hr.fer.proinz.skilletcooking.payload.Request;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
/** A class that contains all the necessary data for a login */
public class RecipeRequest {
    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank(message = "title cannot be empty")
    /** A attribute that contains the username to be authenticated */
    private String title;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank(message = "recipedescription cannot be empty")
    /** A attribute that contains the password to be authenticated */
    private String recipeDescription;

    private Integer estimatedTime;

    private Integer userId;

    private String[] description;

    private String[] name;

    private int[] quantity;

    private String[] measure;

    private MultipartFile[] pictures;

}
