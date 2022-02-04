package hr.fer.proinz.skilletcooking.repository;

import hr.fer.proinz.skilletcooking.models.Rating;
import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A interface that extends the JpaRepository and it is used to access data from
 * the database. In the JpaRepository most of the methods are defined so the
 * only methods that should be implemented are the Rating specific methods for
 * example existsById
 */

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query(value = "select AVG(rating_value) from ratings where recipe_id = ?1", nativeQuery = true)
    Double getAverage(Integer id);

    @Query("select r from Rating r where r.userId = ?1 and r.recipeId = ?2")
    Optional<Rating> findByUserIdAndRecipeId(Integer userId, Integer recipeId);

    @Query("select r from Rating r where r.recipeId = ?1")
    List<Rating> findByRecipeId(Integer recipeId);

    void deleteByUserId(Integer userId);
}
