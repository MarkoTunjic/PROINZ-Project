package hr.fer.proinz.skilletcooking.repository;

import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.models.RecipeStep;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A interface that extends the JpaRepository and it is used to access data from
 * the database. In the JpaRepository most of the methods are defined so the
 * only methods that should be implemented are the REcipeStep specific methods
 * for example existsById
 */
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {
    List<RecipeStep> findByRecipe(Recipe recipe);
}
