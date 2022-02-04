package hr.fer.proinz.skilletcooking.models;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

// this annotations says which table is mapped to this class
@Table(name = "ratings")

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation creates getters, setters, constructors and stuff like that
@Data

/** A class that represents a row from the table ratings */
public class Rating {

    // this annotation says that the attribute is the key of the table ratings
    @Id

    // this annotation says that the column will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** An attribute that represents the id of the rating */
    private Integer id;

    /** An attribute that represents the rating of the recipe */
    private int ratingValue;

    private int userId;

    @Column(name="recipe_id")
    private int recipeId;

    /** A default constructor that creates an empty rating object */
    public Rating() {
    }

    /**
     * A constructor that creates an Rating object from the given parameters
     *
     * @param id          the id of the rating
     * @param ratingValue the rating of the reccipe
     * @param userId      id of the user that rated the recipe
     */

    public Rating(int ratingValue, int userId, int recipeId){
        this.ratingValue = ratingValue;
        this.userId = userId;
        this.recipeId = recipeId;
    }
}
