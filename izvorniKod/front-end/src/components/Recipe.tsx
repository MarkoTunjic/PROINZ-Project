import React, {useEffect, useState} from "react";
import {getPath, putPath} from "../api/api";
import {Link, useParams} from "react-router-dom";
import {IComment, ICommentReturn, IRecipe, Rating} from "../api/models";
import {Carousel, CarouselItem, Container} from "react-bootstrap";
import IngredientDisplay from "./IngredientDisplay";
import RecipeStepDisplay from "./RecipeStepDisplay";
import {getLoggedInUserId, isUserLoggedIn} from "../utils/helpers";
import Select from "react-select";
import Comment from "./Comment";
import PostCommentForm from "./PostCommentForm";

type RecipeParams = {
    id: string;
}

const Recipe = () => {
    const [recipe, setRecipe] = useState<IRecipe | undefined>(undefined);
    const [comments, setComments] = useState<IComment[] | undefined>([]);
    const [currentUserRating, setCurrentUserRating] = useState<number | undefined>(undefined);
    const { id } = useParams<RecipeParams>();

    const getRecipeDetails = async () => {
        const result = await getPath(`/recipes/${id}`, undefined);
        if (result.status == 200) {
            return result.data as IRecipe;
        } else {
            console.error("Error when getting specific recipe");
        }
    }

    const refreshComments = () => {
        const result = getPath('/comments/recipe/' + id, undefined);
        result.then((response) => {
            console.log(response.data);
            const newComments = response.data.map((comment: ICommentReturn) => {
                return {
                    ...comment,
                    postedAt: new Date(comment.postedAt)
                }
            });
            setComments(newComments);
        })
    }

    const getLoggedInUserRating = async () => {
        if (!getLoggedInUserId()){
            return;
        }
        const result = await getPath(`/ratings/specific/${getLoggedInUserId()}/${id}`, undefined);
        if (result.status == 200){
            return result.data as Rating
        } else {
            console.info("User hasn't rated this recipe");
        }
    }

    function handleRatingChange(rating: React.ChangeEvent<HTMLSelectElement>){
        const result = putPath(`/ratings?user_id=${getLoggedInUserId()}&recipe_id=${id}`, {ratingValue: rating.target.value}, undefined);
        setCurrentUserRating(parseInt(rating.target.value));
        result.then(response => {
            console.info("Changed rating to: " + JSON.stringify(response.data))
        })
    }

    useEffect(() => {
        getRecipeDetails().then(recipe => {
            console.log(recipe);
            setRecipe(recipe);

            setComments(recipe?.comments.map((comment) => {
                return {
                    ...comment,
                    postedAt: new Date(comment.postedAt)
                }
            }));
        })

        getLoggedInUserRating().then(rating => {
            console.log(rating);
            setCurrentUserRating(rating?.ratingValue);
        })

    }, []);

    return (
        <div id="recipe" className="d-flex justify-content-center">
            {recipe ?
                <>
                <div className="recipe-info">
                    <h1>{recipe.title}</h1>
                    <p><Link to={"/author/" + recipe.userId}>Author: {recipe.username}</Link></p>
                    <p>Potrebno vrijeme: {recipe.estimatedTime + ' ' +formatMinute(recipe.estimatedTime)}</p>
                    {getLoggedInUserId() &&
                    <div><label htmlFor="rating">Vaša ocjena:</label>
                        <select id="rating"
                                name="rating"
                                size={1}
                                onChange={e => handleRatingChange(e)}
                                value={currentUserRating || '0'}
                        >
                            <option value="0">Bez ocjene</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                    </div>
                    }
                    <p>Prosjecna ocjena: {recipe.ratings.length > 0 && Math.round((
                        recipe.ratings.map(rating => rating.ratingValue)
                            .reduce((a, b) => a + b, 0) / recipe.ratings.length || 0
                    ) * 100) / 100  || 'nije ocijenjeno'} </p>
                </div>
                <Carousel id="recipe-carousel" className="w-100">
                    {recipe && recipe.images.sort((a, b) => {
                        return a.imageOrder - b.imageOrder
                    })
                        .map((image) => {
                            const src = `data:image/jpg;base64, ${image.imageData}`
                            return (
                                <CarouselItem>
                                    <img src={src} className="card-img-top"
                                         alt={`Image number: ${image.imageOrder}`}></img>
                                </CarouselItem>
                            );
                        })}
                </Carousel>
                    <div className="recipe-info">
                        <h3>Opis recepta: </h3>
                            {recipe.recipeDescription.split("\r\n").map((paragraph,index) => {
                            return <p key={index}>{paragraph}</p>
                            })}

                        <h3> Sastojci: </h3>
                        <ul>
                            {recipe.ingredients.sort((a,b) => {return a.ingredientOrder - b.ingredientOrder}).map(ingredient => {
                                return <IngredientDisplay key={ingredient.id}
                                                          ingredientName={ingredient.ingredientName}
                                                          ingredientMeasure={ingredient.ingredientMeasure}
                                                          ingredientQuantity={ingredient.ingredientQuantity} />

                            })}
                        </ul>
                        <h3> Koraci: </h3>
                        <ol>
                        { recipe.recipeSteps.sort((a,b) => {return a.stepOrder - b.stepOrder}).map(recipeStep => {
                            return <RecipeStepDisplay key={recipeStep.id}
                                                      stepDescription={recipeStep.stepDescription}/>
                        })
                        }
                        </ol>
                    </div>
                    <section id="comment-section">
                        <div id="comment-section-top">
                            <h2>Komentari: </h2>
                            <button className="btn"
                                    id="refresh-comments-btn"
                                    onClick={refreshComments}>
                                Osvježi
                            </button>
                        </div>

                        {isUserLoggedIn() && <PostCommentForm recipeId={parseInt(id)}/> }
                        <div className="comment-container">
                            {comments && comments.sort((a,b) => { // @ts-ignore
                                return (a.postedAt - b.postedAt)})
                                .map((comment) => {
                                return <Comment user={comment.user}
                                                commentId = {comment.id}
                                                postedAt={comment.postedAt}
                                                comment={comment.comment}
                                                isAuthor={comment.user.id == recipe?.userId}
                                />
                            })}
                        </div>
                    </section>
                </>:

                <div></div>
            }
        </div>
    );
}

function formatMinute(input: number){
    if ([1, 11,12,13].includes(input)){
        return "minuta"
    }
    if([0,5,6,7,8,9].includes(input % 10)){
        return "minuta"
    } else if ([2,3,4].includes(input % 10)){
        return "minute"
    } else {
        return "minutu"
    }
}



export default Recipe;
