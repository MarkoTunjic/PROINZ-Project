package hr.fer.proinz.skilletcooking.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

// this annotations says which table is mapped to this class
@Table(name = "recipes")

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation creates getters, setters, constructors and stuff like that
@Data

/** A class that represents a row from the table recipes */
public class Recipe {

    // this annotation says that the attribute is the key of the table recipes
    @Id

    // this annotation says that the column will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** An attribute that represents the id of the recipe */
    private Integer id;

    /** An attribute that */
    private int popularity;

    /** An attribute that contains the title of the recipe */
    private String title;

    @CreationTimestamp
    /** An attribute that contains the value of when the recipe was created */
    private Timestamp createdAt;

    @UpdateTimestamp
    /** An attribute that contains the value of when the recipe was last updated */
    private Timestamp lastUpdatedAt;

    /** An attribute that contains the description of the recipe */
    private String recipeDescription;

    /** An attribute that contains the estimated time for creating the recipe */
    private int estimatedTime;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    @JsonManagedReference
    private List<Rating> ratings;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Image> images;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RecipeStep> recipeSteps;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "commented_on_recipe")
    @JsonManagedReference
    private List<Comment> comments;

    @Transient
    private String username;

    @Transient
    private Integer userId;

    /** A default constructor that creates a recipe without attributes */
    public Recipe() {}

    /**
     * A constructor that creates a recipe object from the given values
     *
     * @param popularity
     * @param title
     * @param recipeDescription
     * @param estimatedTime
     */
    public Recipe(int popularity, String title, String recipeDescription,
            int estimatedTime) {
        this.popularity = popularity;
        this.title = title;
        this.recipeDescription = recipeDescription;
        this.estimatedTime = estimatedTime;
    }
}
