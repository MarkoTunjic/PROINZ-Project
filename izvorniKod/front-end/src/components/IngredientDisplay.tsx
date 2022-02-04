import {Ingredient, IngredientDisplayInterface} from "../api/models";


const IngredientDisplay = (props: IngredientDisplayInterface) => {
    return (
        <li>{props.ingredientName}: {props.ingredientQuantity} {props.ingredientMeasure}</li>
    );
}

export default IngredientDisplay;
