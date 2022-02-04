// TODO:
//  Ostali modeli i iskoristi ih u Typescript inputu za axios pozive

import firebase from "firebase/compat";
import User = firebase.User;

export interface RegisterInput {
    firstName: string,
    lastName: string,
    dateOfBirth: Date,
    username: string,
    email: string,
    password: string
}

export interface Rating {
    id: number,
    ratingValue: number,
    userId: number
}

export interface Image {
    id: number,
    imageData: string,
    imageOrder: number
}

export interface Ingredient {
    id: number,
    ingredientOrder: number,
    ingredientName: string,
    ingredientMeasure: string,
    ingredientQuantity: number
}
export interface IngredientDisplayInterface {
    ingredientName: string,
    ingredientMeasure: string,
    ingredientQuantity: number
}

export interface RecipeStep {
    id: number,
    stepOrder: number,
    stepDescription: string
}

export interface RecipeStepDisplayInterface {
    stepDescription: string;
}

export interface ICommentReturn {
    commentedByUser: number,
    commentedOnRecipe: number,
    id: number,
    comment: string,
    postedAt: string,
    user: IUser
}

export interface IComment {
    commentedByUser: number,
    commentedOnRecipe: number,
    id: number,
    comment: string,
    postedAt: Date,
    user: IUser
}

export interface CommentDisplayInterface{
    user: IUser,
    postedAt: Date,
    commentId: number,
    comment: string,
    isAuthor: boolean
}

export interface IRecipe {
    id: number,
    popularity: number,
    title: string,
    createdAt: Date,
    lastUpdatedAt: Date,
    recipeDescription: string,
    estimatedTime: number
    ratings: Rating[],
    images: Image[],
    ingredients: Ingredient[],
    recipeSteps: RecipeStep[],
    comments: ICommentReturn[],
    username: string,
    userId: number,
    jaccard: number | undefined,
    ocjena: number | undefined
}

export interface IRole {
    id: number,
    roleName: string
}
export interface IUser {
    id: number,
    dateOfBirth: string,
    email: string,
    firstName: string,
    lastName: string,
    password: string,
    roleId: string,
    userRole: IRole
    username: string
}

export interface IUserDisplay {
    id: number,
    dateOfBirth: string,
    email: string,
    firstName: string,
    lastName: string,
    username: string
}

export interface PostCommentFormProps {
    recipeId: number;
}

export interface PostCommentRequest {
    userId: number,
    recipeId: number,
    commentText: string
}
