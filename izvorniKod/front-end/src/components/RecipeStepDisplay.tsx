import {RecipeStep, RecipeStepDisplayInterface} from "../api/models";


const IngredientDisplay = (props: RecipeStepDisplayInterface) => {

    return (
        <>
            <li>{props.stepDescription.split("\r\n").map((paragraph) => {return <p> {paragraph}</p>})}</li>
        </>
    );
}

export default IngredientDisplay;
