package hr.fer.proinz.skilletcooking.payload.Request;

import lombok.Data;

@Data
public class PostCommentRequest {
    private Integer recipeId;
    private Integer userId;
    private String commentText;
}
