package hr.fer.proinz.skilletcooking.models;

/**
 * A class that defines a new custom exception that is thrown when a recipe is
 * not found in the databse
 */
public class RecipeNotFoundException extends RuntimeException {

    /**
     * A constructor that creates the new exception and calls the super constructor
     * with a custom string that cointains the id of the recipe that could not be
     * found
     *
     * @param id the id of the recipe that could not be found
     */
    public RecipeNotFoundException(Integer id) {
        super("Could not find recipe with id: " + id);
    }
}
