package hr.fer.proinz.skilletcooking.models;

/**
 * A class that defines a new custom aexception that is thrown when a user is
 * not found in the databse
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * A constructor that creates the new exception and calls the super constructor
     * with a custom string that cointains the id of the user that could not be
     * found
     *
     * @param id the id of the user that could not be found
     */
    public UserNotFoundException(Integer id) {
        super("Could not find user with id: " + id);
    }
}
