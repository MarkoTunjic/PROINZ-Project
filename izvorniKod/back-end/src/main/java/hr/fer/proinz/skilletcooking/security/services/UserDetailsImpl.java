package hr.fer.proinz.skilletcooking.security.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hr.fer.proinz.skilletcooking.models.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

//this annotation generates the equals and hashcode method
@EqualsAndHashCode

// this annotation generates getters
@Getter

/** A class that is used to hold specific user information */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    /** An attribute that represents the user id */
    private Integer id;

    /** An attribute that represents the users username */
    private String username;

    /** An attribute that represents the users email */
    private String email;

    // this annotation is used to ignore json serialization and deserialisation
    @JsonIgnore
    /** An attribute that represents the users password */
    private String password;

    /** An attribute that contains all of the users roles */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * A constructor that creates user details from the given parameters
     *
     * @param id          the id of the user
     * @param username    the users username
     * @param email       the users email
     * @param password    the users password
     * @param authorities the users authorities
     */
    public UserDetailsImpl(Integer id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /** A static method that builds userdetails from the given user */
    public static UserDetailsImpl build(User user) {
        // create user authorites from user roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().getRoleName().name()));

        // return new userDetails
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    /**
     * A method that returns true if the account is not expired and false otherwise
     *
     * @return false if the account is expired and true otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        // always true becouse our app does not require this
        return true;
    }

    /**
     * A method that returns true if the account is not locked and false otherwise
     *
     * @return false if the account is locked and true otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        // always true becouse our app does not require this
        return true;
    }

    /**
     * A method that returns true if the credidentials are not expired and false
     * otherwise
     *
     * @return false if the credidentials are expired and true otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // always true becouse our app does not require this
        return true;
    }

    /**
     * Returns true if the account is enabled and false otherwise
     *
     * @return true if enables false otherwise
     */
    @Override
    public boolean isEnabled() {
        // always true becouse our app does not require this
        return true;
    }

}
