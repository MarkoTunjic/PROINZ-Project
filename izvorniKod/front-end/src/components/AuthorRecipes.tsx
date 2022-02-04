import React, { EffectCallback, useEffect, useState } from "react";
import {Link, Redirect, useParams} from "react-router-dom";
import {deletePath, getPath} from "../api/api";
import {getLoggedInUserId, isLoggedInUserModerator} from "../utils/helpers";
import {Button} from "react-bootstrap";


type AuthorRecipesParams = {
    id: string
}

const AuthorRecipes = () => {
    const [content, setContent] = useState([]);
    const [name, setName] = useState("");
    const [recipe, setRecipes] = useState([]);
    const [mess, setMessage] = useState("")
    const { id } = useParams<AuthorRecipesParams>();
    const authorId = parseInt(id);
    const loggedInUserId = getLoggedInUserId();

    async function handleRecipeDelete(title: string, recipeId: number){
        // eslint-disable-next-line no-restricted-globals
        const out = confirm("Jeste li sigurni da želite izbrisati recept:\n" + title)
        if (out)
            await deletePath('/recipes/' + recipeId)
            window.location.reload()
    }

    const getName = async () => {
        let userFullName = null;
        const result = await getPath('/users/' + id, undefined)
        if (result.status == 200) {
            userFullName = result.data.firstName + ' ' + result.data.lastName;
        }
        setName(userFullName || '')
        getRecipes(id);
    }

    const getRecipes = async (id: string) => {
        const result = await getPath("/recipes/all/" + id, undefined);

        if (result.status == 200) {
            for (let i = 0; i < result.data.length; i++) {
                recipe.push(result.data[i] as never);
            }
            writeRecipes();
        } else {
            setMessage((result.data &&
                result.data.message) ||
                result.message ||
                result.toString);
        }

    }

    const writeRecipes = async () => {
        let content1 = [];
        for (let i = 0; i < recipe.length; i++) {
            let img = recipe[i]["images"][0];
            let src = `data:image/jpg;base64, ${img["imageData"]}`;
            let path = "recipe/edit/" + recipe[i]["id"];
            const href = `/recipes/${recipe[i]["id"]}`
            let a =
                <div className="col-md-4">
                    <div className="card">
                        <img src={src} className="card-img-top" alt="..."></img>
                        <div className="card-body">
                            <h5 className="card-title">{recipe[i]["title"]}</h5>
                            <h6 className="card-subtitle mb-2 text-muted">{recipe[i]["username"]}</h6>
                            <p className="card-text">{recipe[i]["recipeDescription"]}</p>
                            <Link to={href} className="btn mr-2">Pojedinosti o receptu</Link>
                            { loggedInUserId && loggedInUserId == id &&
                                <a href={path} className="btn mr-2"><i className="fas fa-link"></i>Uredi</a>
                            }
                            { (loggedInUserId && loggedInUserId == id || isLoggedInUserModerator()) &&
                                <button id="recipe-delete-btn"
                                    className="btn mr-2"
                                    onClick={() => handleRecipeDelete(recipe[i]["title"], recipe[i]["id"])}
                                >
                                Izbriši
                                </button>
                            }

                        </div>
                    </div>
                </div>;
            content1.push(a)
        }
        for (let i = 0; i < content1.length; i++) {
            content.push(content1[i] as never);
        }
        setContent(content1 as never);
    }

    const premaProsjecnojOcjeni = async () => {
        for (let i = 0; i < recipe.length; i++) {
            const result = await getPath("/ratings/" + recipe[i]["id"], undefined);

            if (result.status == 200) {
                recipe[i]["ocjena"] = result.data as never;
            } else {
                setMessage((result.data &&
                    result.data.message) ||
                    result.message ||
                    result.toString);
            }
        }
        recipe.sort((a, b) => b["ocjena"] - a["ocjena"]);
        writeRecipes();
    }

    const premaPopularnosti = () => {
        recipe.sort((a, b) => b["popularity"] as number - a["popularity"] as number);
        writeRecipes();
    }

    const premaPreporuceno = async () => {
        let prosjecnapopularnost = 0;
        let prosjecnaocjena = 0;
        for (let i = 0; i < recipe.length; i++) {
            const result = await getPath("/ratings/" + recipe[i]["id"], undefined);
            prosjecnapopularnost += recipe[i]["popularity"];

            if (result.status == 200) {
                recipe[i]["ocjena"] = result.data as never;
                prosjecnaocjena += result.data;
            } else {
                setMessage((result.data &&
                    result.data.message) ||
                    result.message ||
                    result.toString);
            }
        }
        prosjecnaocjena /= recipe.length;
        prosjecnapopularnost /= recipe.length;

        recipe.sort((a, b) => {
            let razlomakA = a["ocjena"] / prosjecnaocjena + a["popularity"] / prosjecnapopularnost;
            let razlomakB = b["ocjena"] / prosjecnaocjena + b["popularity"] / prosjecnapopularnost;
            return razlomakB - razlomakA;
        });
        writeRecipes();

    }


    useEffect(() => {
        getName();
    }, [setRecipes]);


    return (
        <div>
            <Link to="/recipes/new" className=" btn "> Dodaj recept</Link>
            Sortiraj po:
            <button className=" btn " onClick={premaPreporuceno}>Prema preporuceno</button>
            <button className=" btn " onClick={premaProsjecnojOcjeni}>Prema prosjecnoj ocjeni</button>
            <button className=" btn " onClick={premaPopularnosti}>Prema popularnosti</button>
            <div className="container mx-auto mt-4">
                <div className="row">
                    {content}
                </div>
            </div>
            {mess && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {mess}
                    </div>
                </div>
            )}
        </div>
    )
}

export default AuthorRecipes;
