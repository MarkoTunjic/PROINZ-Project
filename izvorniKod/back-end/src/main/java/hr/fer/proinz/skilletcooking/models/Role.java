package hr.fer.proinz.skilletcooking.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation generates getters, setters constructors and stuff like that
@Data

// this annotations says which table is mapped to this class
@Table(name = "roles")

/** A class that represents a table row from the table users */
public class Role {

    // this annotation says that the attribute is the key of the table Role
    @Id

    // this annotation says that the column will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** A attribute that represents the id of the role */
    private int id;

    // this annotation ensures that attribute won't be blank
    @NotBlank

    // this annotation says that the column the attribute is unique in the database
    @Column(unique = true)

    // this annotation says that the value that will be inserted inside the table
    // will be a string value of the enum ERole
    @Enumerated(EnumType.STRING)

    /** A attribute that represents the role name */
    private ERole roleName;

    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    @JsonManagedReference
    private List<User> users;
     */

    public Role() {}

    /**
     * A constructor that creates a role object from the given values
     *
     * @param id       the id of the role
     * @param roleName the name of the role
     */
    public Role(int id, ERole roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
