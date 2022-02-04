package hr.fer.proinz.skilletcooking.payload.Request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

//this annotation generates getters
@Getter

// this annotation generates setters
@Setter

/** A class that contains all the necessary data for a login */
public class RecipeEditRequest {
    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank(message = "title cannot be empty")
    /** A attribute that contains the username to be authenticated */
    private String title;

    // this annotation says that the attribute won't be null neither empty neither
    // full of whitespaces
    @NotBlank(message = "recipe description cannot be empty")
    /** A attribute that contains the password to be authenticated */
    private String recipeDescription;

    @NotBlank(message = "estimatedtime cannot be empty")
    private Integer estimatedTime;

    @NotBlank(message = "userid cannot be empty")
    private Integer userId;

    @NotBlank(message = "recipe steps cannot be empty")
    private String recipeSteps;

    @NotBlank(message = "ingredients cannot be empty")
    private String ingredients;

}
