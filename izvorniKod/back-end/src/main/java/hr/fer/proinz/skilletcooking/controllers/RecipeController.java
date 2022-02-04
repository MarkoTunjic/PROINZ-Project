package hr.fer.proinz.skilletcooking.controllers;

import hr.fer.proinz.skilletcooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hr.fer.proinz.skilletcooking.models.Image;
import hr.fer.proinz.skilletcooking.models.Ingredient;
import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.repository.ImageRepository;
import hr.fer.proinz.skilletcooking.repository.IngredientRepository;
import hr.fer.proinz.skilletcooking.repository.RecipeRepository;
import hr.fer.proinz.skilletcooking.repository.RecipeStepRepository;
import hr.fer.proinz.skilletcooking.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import hr.fer.proinz.skilletcooking.models.RecipeNotFoundException;
import hr.fer.proinz.skilletcooking.models.RecipeStep;
import hr.fer.proinz.skilletcooking.models.User;
import hr.fer.proinz.skilletcooking.payload.Request.RecipeRequest;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//this annotation says that this class will be a rest controller and creates a Bean object that will
//be saved in the spring container
@RestController

// this annotation defines that we receive requests from all sources
@CrossOrigin(origins = "*", maxAge = 3600)

// this annotation says that this controller controlls only requests with the
// /api/recipes destination
@RequestMapping("/api/recipes")

/**
 * A class that contains methods that define what to do when a HTTP request
 * comes to the server
 */

public class RecipeController {

    // this annotation says that the recipe repository will be automatically
    // injected
    // from the spring container
    @Autowired
    /**
     * A private attribute that contains the reference to the recipe repository
     * which
     * is used to acces data from the database
     */
    private RecipeRepository repository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserRepository userRepository;

    // this annotation says that this method handles GET requests for the
    // /api/recipes/ destination
    @GetMapping("")
    /**
     * A method that returns a list of all recipes found in the database
     *
     * @return list of all recipes from the database
     */
    List<Recipe> all() {
        List<Recipe> recipes = repository.findAll();
        recipes.forEach(r -> r.setUsername(r.getUser().getUsername()));
        return recipes;
    }

    // this annotation says that this method handles GET requests for the
    // /api/recipes/author/id destination
    @GetMapping("/author/{id}")
    /**
     * A method that returns a list of all recipes found in the database
     *
     * @return list of all recipes from the database
     */
    User author(@PathVariable int id) {
        return repository.findById(id).get().getUser();
    }

    // this annotation says that this method handles POST requests for the
    // /api/recipes destination
    @Transactional
    @PostMapping(value = "/", consumes = { "multipart/form-data" })
    @PreAuthorize("hasAuthority('USER')")
    /**
     * A method that inserts a new recipe into the database and returns the recipe
     * it
     * inserted
     *
     * @param payload JSON mapping of recipe attributes in post request
     */
    Recipe newRecipe(@ModelAttribute RecipeRequest payload) {
        User user;
        try {
            user = userRepository.findById(payload.getUserId()).get();
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Could not find user with given id or id is null");
        }

        RecipeData newRecipe = getNewRecipeData(payload, user);

        Recipe savedRecipe = repository.save(newRecipe.getRecipe());
        imageRepository.saveAll(newRecipe.getImages());
        ingredientRepository.saveAll(newRecipe.getIngredients());
        recipeStepRepository.saveAll(newRecipe.getSteps());
        return savedRecipe;
    }

    public static RecipeData getNewRecipeData(RecipeRequest payload, User user) {
        try {
            Recipe newRecipe;
            if (payload.getTitle().length() == 0 || payload.getRecipeDescription().length() == 0 ||
                    payload.getEstimatedTime() < 0) {
                throw new IllegalArgumentException(
                        "Title and short recipe description can not be empty and estimated time can not be less than 0");
            }

            for (String recipeStep : payload.getDescription()) {
                if (recipeStep.length() > 250) {
                    throw new IllegalArgumentException("Recipe step can not be bigger than 250");
                }
            }

            if (payload.getTitle().length() > 50) {
                throw new IllegalArgumentException("Title can not be bigger than 50");
            }

            if (payload.getRecipeDescription().length() > 500) {
                throw new IllegalArgumentException("Recipe description can not be bigger than 500");
            }

            for (String name : payload.getName()) {
                if (name.length() > 25) {
                    throw new IllegalArgumentException("Ingredient name can not be bigger than 25");
                }
            }

            for (String measure : payload.getMeasure()) {
                if (measure.length() > 25) {
                    throw new IllegalArgumentException("Ingredient measure can not be bigger than 25");
                }
            }

            newRecipe = new Recipe(0, payload.getTitle(), payload.getRecipeDescription(),
                    payload.getEstimatedTime());
            newRecipe.setUser(user);

            List<Image> images = new ArrayList<>();
            if (payload.getPictures().length > 5)
                throw new IllegalArgumentException("Maximum 5 pictures are allowed");
            if (payload.getPictures().length == 0)
                throw new IllegalArgumentException("A picture is required");

            for (int i = 0; i < payload.getPictures().length; i++) {
                Image newImage;
                try {
                    newImage = new Image(payload.getPictures()[i].getBytes(), i + 1);
                    newImage.setRecipe(newRecipe);
                    images.add(newImage);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Could not read image");
                }
            }

            if (payload.getName().length == 0 || payload.getQuantity().length == 0 || payload.getMeasure().length == 0)
                throw new IllegalArgumentException("No complete ingredients were given");

            if (payload.getName().length != payload.getQuantity().length
                    || payload.getName().length != payload.getMeasure().length
                    || payload.getQuantity().length != payload.getMeasure().length)
                throw new IllegalArgumentException("Some ingredients are missing data");

            List<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < payload.getName().length; i++) {
                if (payload.getName()[i].length() == 0 || payload.getMeasure()[i].length() == 0
                        || payload.getQuantity()[i] < 1)
                    throw new IllegalArgumentException("Ingredient parts can not be empty or negative");
                Ingredient newIngredient = new Ingredient(i + 1, payload.getName()[i], payload.getMeasure()[i],
                        payload.getQuantity()[i]);
                newIngredient.setRecipe(newRecipe);
                ingredients.add(newIngredient);
            }

            if (payload.getDescription().length == 0)
                throw new IllegalArgumentException("Missing recipe steps");

            List<RecipeStep> recipeSteps = new ArrayList<>();
            for (int i = 0; i < payload.getDescription().length; i++) {
                if (payload.getDescription()[i].length() == 0)
                    throw new IllegalArgumentException("Empty step description was given");

                RecipeStep newRecipeStep = new RecipeStep(i + 1, payload.getDescription()[i]);
                newRecipeStep.setRecipe(newRecipe);
                recipeSteps.add(newRecipeStep);
            }
            return new RecipeData(newRecipe, ingredients, recipeSteps, images);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Missing arguments for recipe");
        }

    }

    @Getter
    @AllArgsConstructor
    public static class RecipeData {
        private Recipe recipe;
        private List<Ingredient> ingredients;
        private List<RecipeStep> steps;
        private List<Image> images;
    }

    // this annotation says that this method handles GET requests for the
    // /api/recipes/all/*someId* destination
    @GetMapping("/all/{id}")

    /**
     * A method that returns a single recipe which matches the given id. If the
     * recipes
     * does not exist a RecipeNotFoundException is thrown
     *
     * @param id the id of the recipe to be searched for it is taken from the link
     *
     * @return the recipe with the given id
     *
     * @throws RecipeNotFoundException if the recipe does exist in the database
     */

    List<Recipe> allRecipesById(@PathVariable Integer id) {
        User createdBy = userRepository.findById(id).get();
        List<Recipe> recipes = repository.findByUser(createdBy);
        recipes.forEach(r -> r.setUsername(r.getUser().getUsername()));
        return recipes;
    }

    // this annotation says that this method handles GET requests for the
    // /api/recipes/*someId* destination
    @GetMapping("/{id}")

    /**
     * A method that returns a single recipe which matches the given id. If the
     * recipes
     * does not exist a RecipeNotFoundException is thrown
     *
     * @param id the id of the recipe to be searched for it is taken from the link
     *
     * @return the recipe with the given id
     *
     * @throws RecipeNotFoundException if the recipe does exist in the database
     */
    Recipe one(@PathVariable Integer id) {
        Recipe recipe = repository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        // popularnost se inkrementa svaki put kad se prikaže pojedini recept
        recipe.setPopularity(recipe.getPopularity() + 1);
        repository.save(recipe);
        recipe.setUserId(recipe.getUser().getId());
        recipe.setUsername(recipe.getUser().getUsername());
        return recipe;
    }

    // this annotation says that this method handles PUT requests for the
    // /api/recipes/*someId* destination
    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR')")
    /**
     * A method that tries to find a recipe with given id and changes their values
     * to the given new values
     * if the recipe does not exist the new recipe will be inserted
     * into the database
     *
     *
     * @param newRecipe the newValues to be put to the given id or inserted into the
     *                  databse if the given id did not exist
     * @param id        the id of the recipe to be searched for it is taken from the
     *                  link
     *
     * @return the recipe with the given id
     */
    Recipe replaceRecipe(@ModelAttribute RecipeRequest payload, @PathVariable Integer id) {

        if (payload.getTitle().length() == 0 || payload.getRecipeDescription().length() == 0 ||
                payload.getEstimatedTime() < 0) {
            throw new IllegalArgumentException(
                    "Title and short recipe description can not be empty and estimated time can not be less than 0");
        }

        for (String recipeStep : payload.getDescription()) {
            if (recipeStep.length() > 250) {
                throw new IllegalArgumentException("Recipe step can not be bigger than 250");
            }
        }

        if (payload.getTitle().length() > 50) {
            throw new IllegalArgumentException("Title can not be bigger than 50");
        }

        if (payload.getRecipeDescription().length() > 500) {
            throw new IllegalArgumentException("Recipe description can not be bigger than 500");
        }

        for (String name : payload.getName()) {
            if (name.length() > 25) {
                throw new IllegalArgumentException("Ingredient name can not be bigger than 25");
            }
        }

        for (String measure : payload.getMeasure()) {
            if (measure.length() > 25) {
                throw new IllegalArgumentException("Ingredient measure can not be bigger than 25");
            }
        }

        Recipe oldRecipe = repository.findById(id).get();

        try {
            oldRecipe.setTitle(payload.getTitle());
            oldRecipe.setEstimatedTime(payload.getEstimatedTime());
            oldRecipe.setRecipeDescription(payload.getRecipeDescription());

        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Missing arguments for recipe");
        }

        if (payload.getName().length == 0 || payload.getQuantity().length == 0 ||
                payload.getMeasure().length == 0)
            throw new IllegalArgumentException("No complete ingredients were given");

        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < payload.getName().length; i++) {
            if (payload.getName().length != payload.getQuantity().length
                    || payload.getName().length != payload.getMeasure().length
                    || payload.getQuantity().length != payload.getMeasure().length)
                throw new IllegalArgumentException("Ingredients missing data");
            if (payload.getName()[i].length() == 0 || payload.getMeasure()[i].length() == 0
                    || payload.getQuantity()[i] < 1)
                throw new IllegalArgumentException("Ingredient parts can not be empty or negative");
            Ingredient newIngredient = new Ingredient(i + 1, payload.getName()[i],
                    payload.getMeasure()[i],
                    payload.getQuantity()[i]);
            newIngredient.setRecipe(oldRecipe);
            ingredients.add(newIngredient);
        }

        if (payload.getDescription().length == 0)
            throw new IllegalArgumentException("Missing recipe steps");

        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (int i = 0; i < payload.getDescription().length; i++) {
            if (payload.getDescription()[i].length() == 0)
                throw new IllegalArgumentException("Empty step description was given");

            RecipeStep newRecipeStep = new RecipeStep(i + 1,
                    payload.getDescription()[i]);
            newRecipeStep.setRecipe(oldRecipe);
            recipeSteps.add(newRecipeStep);
        }
        ingredientRepository.deleteAll(ingredientRepository.findByRecipe(oldRecipe));
        recipeStepRepository.deleteAll(recipeStepRepository.findByRecipe(oldRecipe));
        oldRecipe.setIngredients(ingredients);
        oldRecipe.setRecipeSteps(recipeSteps);
        oldRecipe = repository.save(oldRecipe);

        return oldRecipe;

    }

    // this annotation says that this method handles DELETE requests for the
    // /api/recipes/*someId* destination
    @DeleteMapping("/{id}")
    /**
     * A method that deletes the recipe with the given id
     *
     * @param id the id of the recipe that will be deleted, its value is taken from
     *           the link
     */
    void deleteRecipe(@PathVariable Integer id) {
        System.out.println("GOT DELETE RECIPE REQUEST FOR ID " + id);
        ratingRepository.deleteAll(ratingRepository.findByRecipeId(id));
        commentRepository.deleteAll(commentRepository.findByRecipeId(id));
        repository.deleteById(id);
    }
}
