package hr.fer.proinz.skilletcooking.models;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

// this annotations says which table is mapped to this class
@Table(name = "images")

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation creates getters, setters, constructors and stuff like that
@Data

/** A class that represents a row from the table images */
public class Image {

    // this annotation says that the attribute is the key of the table images
    @Id

    // this annotation says that the column will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** An attribute that represents the id of the image */
    private Integer id;

    /** An attribute that contains the image in byte format */
    private byte[] imageData;

    /** An attribte that contains the cardinal number of the image */
    private int imageOrder;

    @ManyToOne
    @JsonBackReference
    private Recipe recipe;

    /** A default constructor that creates an empty image */
    public Image() {}

    /**
     * A constructor that creates an image from the given parameters
     *
     * @param imageData  the image data in byte format
     * @param imageOrder the cardinal number of the image
     * @param recipeId   the corresponding recipe id of this image
     */
    public Image(byte[] imageData, int imageOrder) {
        this.imageData = imageData.clone();
        this.imageOrder = imageOrder;
    }

}
