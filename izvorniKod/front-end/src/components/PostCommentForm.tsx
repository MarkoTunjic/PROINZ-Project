import {Button, FormText} from "react-bootstrap";
import {PostCommentFormProps, PostCommentRequest} from "../api/models";
import {postPath} from "../api/api";
import {getLoggedInUserId} from "../utils/helpers";

const PostCommentForm = (props: PostCommentFormProps) => {

    function handleCommentPost() {
        const textarea = document.getElementById("comment-textarea") as HTMLInputElement;
        postPath('/comments', {
            commentText: textarea.value,
            recipeId: props.recipeId,
            userId: getLoggedInUserId()
        } as PostCommentRequest, undefined);
        textarea.value = "";
    }

    return (
        <div className="post-comment-form">
            <textarea id="comment-textarea" placeholder="Type your comment here"></textarea>
            <button className="btn" onClick={handleCommentPost}>Post comment</button>
        </div>
    );
}

export default PostCommentForm;
