package hr.fer.proinz.skilletcooking.models;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

// this annotations says which table is mapped to this class
@Table(name = "recipe_steps")

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation creates getters, setters, constructors and stuff like that
@Data

/** A class that represents a row from the table recipe_steps */
public class RecipeStep {

    // this annotation says that the attribute is the key of the table recipe_steps
    @Id

    // this annotation says that the column will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** An attribute that represents the id of the recipe step */
    private Integer id;

    /** An attribute that contains the cardinal number of the recipe step */
    private int stepOrder;

    /** An attribute that contains the description of the step */
    private String stepDescription;

    @ManyToOne
    @JsonBackReference
    private Recipe recipe;

    /** A default constructor that creates an empty recipe step */
    public RecipeStep() {}

    /**
     * A constructor that creates an recipe step object from the given parameters
     *
     * @param stepOrder       the cardinal number of this step
     * @param stepDescription the description of this step
     */
    public RecipeStep(int stepOrder, String stepDescription) {
        this.stepOrder = stepOrder;
        this.stepDescription = stepDescription;
    }
}
