package hr.fer.proinz.skilletcooking.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.List;

// this annotations says which table is mapped to this class
@Table(name = "users")

// this annotation says that this is a class used for ORM and that it is a Bean
@Entity

// this annotation creates getters, setters, constructors and stuff like that
@Data

/**
 * A class that represents a object representation of the table users
 * (ORM-Object relational mapping)
 */
public class User {

    // this annotation says that this attribute is the id of the table
    @Id

    // this annotation says that this attribute will be self incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** An private attribute that represents the id of the user */
    private Integer id;

    // this annotation ensures that attribute won't be blank
    @NotBlank
    /** An private attribute that represents the users firstName */
    private String firstName;

    // this annotation ensures that attribute won't be blank
    @NotBlank
    /** An private attribute that represents the users last name */
    private String lastName;

    // this annotation ensures that attribute won't be blank
    @NotBlank
    // this annotation says that this attribute that represents the column email
    // from the table is unique
    @Column(unique = true)

    // this annotation ensures that attribute won't be blank
    @NotBlank
    /** An attribute that represents the username */
    private String username;

    /** An private attribute that represents the users email */
    private String email;

    @Column(name="role_id", insertable = false, updatable = false)
    private Integer roleId;

    // this annotation ensures that attribute won't be blank
    @NotBlank
    // name of the password column in the Database
    @Column(name = "password_hash")
    /**
     * An private attribute that represents the users password (it will be encrypted
     * using Bcrypt algorithm)
     */
    private String password;

    /** An private attribute that represents the users date of birth */
    private LocalDate dateOfBirth;

    // this annotation says that the connection between the user and the role is
    // many to one and that the value will be fetched whenever we load the roles
    // table
    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    /** An private attribute that contains the role name of the user */
    private Role userRole;

    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    private List<Recipe> recipe;
     */

    /** A default constructor that creates an user without attributes */
    public User() {}

    /**
     * A constructor that creates an user object from the given values
     *
     * @param firstName   the users first name
     * @param lastName    the users last name
     * @param email       the users email
     * @param password    the users password
     * @param dateOfBirth the users date of birth
     * @param roleId      id of role assigned to user
     */
    public User(String firstName, String lastName, String email, String password, LocalDate dateOfBirth,
            String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
    }

    public User(String firstName, String lastName, String email, String password, String dateOfBirth, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.username = username;
    }

}
