package hr.fer.proinz.skilletcooking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="comments")
@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="commented_by_user")
    private Integer commentedByUser;

    @Column(name="commented_on_recipe")
    private Integer commentedOnRecipe;

    @Column(name="comment_text")
    private String comment;

    @Column(name="posted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedAt;

    @OneToOne
    @JoinColumn(name="commented_by_user", referencedColumnName = "id", insertable = false, updatable = false)
    User user;

    public Comment() {}

    public Comment(int commentedByUser, int commentedOnRecipe, String comment){
        this.commentedByUser = commentedByUser;
        this.commentedOnRecipe = commentedOnRecipe;
        this.comment = comment;
        this.postedAt = new Date();
    }

    public Comment(int commentedByUser, int commentedOnRecipe, String comment, Date postedAt){
        this.commentedByUser = commentedByUser;
        this.commentedOnRecipe = commentedOnRecipe;
        this.comment = comment;
        this.postedAt = postedAt;
    }
}
