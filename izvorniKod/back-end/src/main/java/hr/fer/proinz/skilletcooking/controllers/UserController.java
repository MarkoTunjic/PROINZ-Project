package hr.fer.proinz.skilletcooking.controllers;

import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.repository.CommentRepository;
import hr.fer.proinz.skilletcooking.repository.RatingRepository;
import hr.fer.proinz.skilletcooking.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hr.fer.proinz.skilletcooking.models.User;
import hr.fer.proinz.skilletcooking.models.UserNotFoundException;
import hr.fer.proinz.skilletcooking.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//this annotation says that this class will be a rest controller and creates a Bean object that will
//be saved in the spring container
@RestController

// this annotation defines that we recevie requests from all sources
@CrossOrigin(origins = "*", maxAge = 3600)

// this annotation says that this controller controlls only requests with the
// /api/users destination
@RequestMapping("/api/users")

/**
 * A class that contains methods that define what to do when a HTTP request
 * comes to the server
 */
public class UserController {

    // this annotation says that the user repository will be automatically injected
    // from the spring container
    @Autowired
    /**
     * A private attribute that contains the reference to the user repository which
     * is used to acces data from the database
     */
    private UserRepository repository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    // this annotation says that this method handles GET requests for the
    // /api/users/ destination
    @GetMapping("")

    /**
     * A method that returns a list of all users found in the database
     *
     * @return list of all users from the database
     */
    List<User> all() {
        return repository.findAll();
    }

    // this annotation says that this method handles POST requests for the
    // /api/users/ destination
    @PostMapping("")
    /**
     * A method that inserts a new user into the database and returns the user it
     * inserted
     *
     * @param payload JSON mapping of user attributes in post request
     */
    User newUser(@RequestBody Map<String, String> payload) throws Exception {
        try {
            // ako definiramo jednaka imena u input JSON-u možemo koristiti
            // primjer JSON inputa koji ovo parsira:
            // {
            // "first_name":"stipe",
            // "last_name":"stipic",
            // "email":"stipe_stipic@mojmail.net",
            // "password":"stipetovpassword",
            // "date_of_birth":"2000-08-20",
            // "role_id":1
            // }
            // @RequestBody User newUser
            // repository.save(newUser)
            User newUser = new User(payload.get("first_name"), payload.get("last_name"), payload.get("email"),
                    payload.get("password"), LocalDate.parse(payload.get("date_of_birth")), payload.get("username"));
            return repository.save(newUser);
        } catch (Exception e) {
            throw new Exception("Failed creating user: " + payload.toString() + " " + e.getMessage());
        }
    }

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
    User one(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    // this annotation says that this method handles PUT requests for the
    // /api/users/*someId* destination
    @PutMapping("/{id}")

    /**
     * A method that tries to find a user with given id and changes their values to
     * the given new values if the user does not exist the new user will be inserted
     * into the databse
     *
     *
     * @param newUser the newValues to be put to the given id or inserted into the
     *                databse if the given id did not exist
     * @param id      the id of the user to be searched for it is taken from the
     *                link
     *
     * @return the user with the given id
     */
    User replaceUser(@RequestBody User newUser, @PathVariable Integer id) {
        return repository.findById(id).map(user -> {
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            user.setDateOfBirth(newUser.getDateOfBirth());
            return repository.save(user);
        }).orElseGet(() -> {
            newUser.setId(id);
            return repository.save(newUser);
        });
    }

    // this annotation says that this method handles DELETE requests for the
    // /api/users/*someId* destination
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    /**
     * A method that deletes the user with the given id
     *
     * @param id the id of the user to be deletes its value is taken from the link
     */
    void deleteUser(@PathVariable Integer id) {
        User deleteUser = repository.findById(id).get();
        System.out.println(deleteUser);
        List<Recipe> userRecipes = recipeRepository.findByUser(deleteUser);
        userRecipes.forEach((recipe) -> {
            ratingRepository.deleteAll(ratingRepository.findByRecipeId(recipe.getId()));
            commentRepository.deleteAll(commentRepository.findByRecipeId(recipe.getId()));
            recipeRepository.delete(recipe);
        });
        commentRepository.deleteByUser(deleteUser);
        ratingRepository.deleteByUserId(id);
        repository.deleteById(id);
    }
}
