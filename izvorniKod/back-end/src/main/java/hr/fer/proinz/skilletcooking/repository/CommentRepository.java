package hr.fer.proinz.skilletcooking.repository;

import hr.fer.proinz.skilletcooking.models.Comment;
import hr.fer.proinz.skilletcooking.models.Recipe;
import hr.fer.proinz.skilletcooking.models.RecipeStep;
import hr.fer.proinz.skilletcooking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select c from Comment c where c.commentedOnRecipe = ?1")
    List<Comment> findByRecipeId(Integer recipeId);

    @Modifying
    @Query("delete from Comment c where c.commentedOnRecipe = ?1")
    void delByRecipeId(Integer commentedOnRecipe);

    void deleteByUser(User user);
}
