package hr.fer.proinz.skilletcooking.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.proinz.skilletcooking.models.Ingredient;
import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.repository.IngredientRepository;
import hr.fer.proinz.skilletcooking.repository.RecipeRepository;

//this annotation says that this class will be a rest controller and creates a Bean object that will
//be saved in the spring container
@RestController

// this annotation defines that we recevie requests from all sources
@CrossOrigin(origins = "*", maxAge = 3600)

// this annotation says that this controller controlls only requests with the
// /api/ingredients destination
@RequestMapping("/api/ingredients")

public class IngredientController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    // this annotation says that this method handles GET requests for the
    // /api/users/*someId* destination
    @GetMapping("/{id}")

    /**
     * A method that returns a single user which matches the given id. If the users
     * does not exist a UserNotFoundException is thrown
     *
     * @param id the id of the user to be searched for it is taken from the link
     *
     * @return the user with the given id
     *
     * @throws UserNotFoundException if the user does exist in the database
     */
    List<Ingredient> getAllIngredientsById(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id.intValue()).get();
        List<Ingredient> ingredients = ingredientRepository.findByRecipe(recipe);
        return ingredients;
    }
}
