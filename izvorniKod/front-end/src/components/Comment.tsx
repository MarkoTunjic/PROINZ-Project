import {CommentDisplayInterface, Ingredient, IngredientDisplayInterface} from "../api/models";
import {getLoggedInUserId, isLoggedInUserModerator, isUserModerator, splitStringByNewlines} from "../utils/helpers";
import {deletePath} from "../api/api";


const Comment = (props: CommentDisplayInterface) => {

    async function handleDelete(){
        // eslint-disable-next-line no-restricted-globals
        const out = confirm("Jeste li sigurni da želite izbrisati komentar:\n" + props.comment)
        if (out)
            deletePath('/comments/' + props.commentId)
    }

    return (
        <div className="comment">
            <div className="comment-info">
                <div className="user-info">
                    {isUserModerator(props.user) &&
                    <img src="../moderator_icon.png" className="comment-icon"/>
                    }
                    {props.isAuthor &&
                    <img src="../author_icon.png" className="comment-icon"/>
                    }
                    <span className="fw-bold">{props.user.username}</span>

                </div>
                <span className="comment-date">{formatDate(props.postedAt)}</span>
                { (isLoggedInUserCommentAuthor() || isLoggedInUserModerator()) &&
                <button className="btn h-100 delete-btn" onClick={handleDelete}>Delete comment</button>
                }
            </div>
            <div className="comment-text">
                {
                    splitStringByNewlines(props.comment).map((paragraph, index) => {
                        return <p key={index}>{paragraph}</p>
                    })
                }
            </div>
        </div>
    );

    function isLoggedInUserCommentAuthor() {
        return getLoggedInUserId() == props.user.id;
    }
}

function formatDate(date: Date){
    return `${date.toDateString()} ${formatNumber(date.getHours())}:${formatNumber(date.getMinutes())}`
}

function formatNumber(input: number){
    if (input <= 9)
        return "0" + input.toString();
    return input.toString()

}


export default Comment;
