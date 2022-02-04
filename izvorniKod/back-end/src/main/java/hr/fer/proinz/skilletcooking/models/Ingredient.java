package hr.fer.proinz.skilletcooking.models;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

// this annotations says which table is mapped to this class
@Table(name = "ingredients")

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation creates getters, setters, constructors and stuff like that
@Data

/** A class that represents a row from the table ingredients */
public class Ingredient {

    // this annotation says that the attribute is the key of the table ingredients
    @Id

    // this annotation says that the column will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** An attribute that represents the id of the ingredients */
    private Integer id;

    /** An atrribute that represents the cardinal number of the ingredient */
    private int ingredientOrder;

    /** An attribute that represents the name of the ingredient */
    private String ingredientName;

    /**
     * A attribute that represents the meausre used for the measuring of ingredients
     * for example liter, gram, cup etc.
     */
    private String ingredientMeasure;

    /** A attribute that represents the quantitiy of this ingredients */
    private int ingredientQuantity;

    @ManyToOne
    @JsonBackReference
    private Recipe recipe;

    /** A default constructor that creates an empty ingredient */
    public Ingredient() {}

    /**
     * A constructor that creates an ingredient object from the given parameters
     *
     * @param ingredientOrder    the cardinal number of the ingredient
     * @param ingredientName     the name of the ingredient
     * @param ingredientMeasure  the measure for the ingredient
     * @param ingredientQuantity the quantity of the ingredient
     * @param recipeId           the corresponding recipe id for this ingredient
     */
    public Ingredient(int ingredientOrder, String ingredientName, String ingredientMeasure, int ingredientQuantity) {
        this.ingredientOrder = ingredientOrder;
        this.ingredientName = ingredientName;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientQuantity = ingredientQuantity;
    }

}
