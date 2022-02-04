package hr.fer.proinz.skilletcooking.repository;

import hr.fer.proinz.skilletcooking.models.Ingredient;
import hr.fer.proinz.skilletcooking.models.Recipe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A interface that extends the JpaRepository and it is used to access data from
 * the database. In the JpaRepository most of the methods are defined so the
 * only methods that should be implemented are the Ingredient specific methods
 * for example existsById
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByRecipe(Recipe recipe);
}
