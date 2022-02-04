package hr.fer.proinz.skilletcooking.controllers;

import hr.fer.proinz.skilletcooking.models.Comment;
import hr.fer.proinz.skilletcooking.payload.Request.PostCommentRequest;
import hr.fer.proinz.skilletcooking.repository.CommentRepository;
import javassist.NotFoundException;
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

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/comments")
public class  CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    List<Comment> all() {
        return commentRepository.findAll();
    }

    @GetMapping("/recipe/{recipeId}")
    List<Comment> commentsForRecipe(@PathVariable Integer recipeId) {
        return commentRepository.findByRecipeId(recipeId);
    }

    @PostMapping("")
    Comment newComment(@RequestBody PostCommentRequest payload) throws Exception {
        try {
            Comment newComment = new Comment(payload.getUserId(), payload.getRecipeId(), payload.getCommentText());
            return commentRepository.save(newComment);
        } catch (Exception e) {
            throw new Exception("Failed posting comment: " + payload.toString() + " " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    Comment one(@PathVariable Integer id) throws NotFoundException {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment with " + id + " not found"));
    }

    @PutMapping("/{id}")
    Comment replaceComment(@RequestBody Comment newComment, @PathVariable Integer id) {
        return commentRepository.findById(id).map(comment -> {
            comment.setComment(newComment.getComment());
            comment.setCommentedByUser(newComment.getCommentedByUser());
            comment.setCommentedOnRecipe(newComment.getCommentedOnRecipe());
            comment.setPostedAt(newComment.getPostedAt());
            return commentRepository.save(comment);
        }).orElseGet(() -> {
            newComment.setId(id);
            return commentRepository.save(newComment);
        });
    }

    @DeleteMapping("/{id}")
    void deleteComment(@PathVariable Integer id) {
        commentRepository.deleteById(id);
    }
}
