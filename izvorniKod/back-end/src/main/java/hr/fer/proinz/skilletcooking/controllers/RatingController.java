package hr.fer.proinz.skilletcooking.controllers;

import hr.fer.proinz.skilletcooking.models.Rating;
import hr.fer.proinz.skilletcooking.models.User;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hr.fer.proinz.skilletcooking.repository.RatingRepository;

//this annotation says that this class will be a rest controller and creates a Bean object that will
//be saved in the spring container
@RestController

// this annotation defines that we receive requests from all sources
@CrossOrigin(origins = "*", maxAge = 3600)

// this annotation says that this controller controlls only requests with the
// /api/recipes destination
@RequestMapping("/api/ratings")

public class RatingController {
    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping("/{id}")
    Double ratingFor(@PathVariable Integer id) {
        return ratingRepository.getAverage(id);
    }

    @GetMapping("/specific/{userid}/{recipeid}")
    Rating getOwnRating(@PathVariable Integer userid, @PathVariable Integer recipeid) throws NotFoundException {
        return ratingRepository.findByUserIdAndRecipeId(userid, recipeid).orElse(null);
    }

    @PutMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR')")
    Rating replaceRating(@RequestBody Rating newRating,
                         @RequestParam(name="user_id") Integer userId,
                         @RequestParam(name="recipe_id") Integer recipeId) {
        return ratingRepository.findByUserIdAndRecipeId(userId, recipeId).map(rating -> {
            if (newRating.getRatingValue() == 0) {
                ratingRepository.deleteById(rating.getId());
                return null;
            }
            rating.setRatingValue(newRating.getRatingValue());
            return ratingRepository.save(rating);
        }).orElseGet(() -> {
            if (newRating.getRatingValue() == 0){
                return null;
            }
            Rating savedRating = new Rating(newRating.getRatingValue(), userId, recipeId);
            return ratingRepository.save(savedRating);
        });
    }
}
