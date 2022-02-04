package hr.fer.proinz.skilletcooking.repository;

import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.models.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A interface that extends the JpaRepository and it is used to access data from
 * the database. In the JpaRepository most of the methods are defined so the
 * only methods that should be implemented are the Recipe specific methods for
 * example existsById
 */
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByUser(User user);
    void deleteByUser(Optional<User> user);
}
