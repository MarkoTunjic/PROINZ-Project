import React, { useEffect, useState } from "react";
import { getPath } from "../api/api";
import { Link, Redirect } from "react-router-dom";
import { Ingredient, IRecipe } from "../api/models";
import Select from "react-select";

type OptionsType = {
    value: string,
    label: string
}
const Home = () => {
    const [content, setContent] = useState([]);
    const [name, setName] = useState("");
    const [recipes, setRecipes] = useState<IRecipe[]>([]);
    const [mess, setMessage] = useState("");
    const [options, setOptions] = useState<OptionsType[]>([]);
    const [filter, setFilter] = useState<string[]>([]);
    const getName = async () => {
        const userLocal = localStorage.getItem("user");
        const userInfo = userLocal ? JSON.parse(userLocal) : null;
        let userFullName = null;
        if (userInfo) {
            setName(userFullName || userInfo.username)
        }
    }

    const getRecipes = async () => {
        const result = await getPath("/recipes", undefined);
        if (result.status == 200) {
            if (recipes.length == 0) {
                for (let i = 0; i < result.data.length; i++) {
                    recipes.push(result.data[i] as IRecipe);
                }
                let a = new Set<string>();
                for (let recipe of recipes) {
                    for (let ingredient of recipe.ingredients) {
                        const ingredientName = ingredient.ingredientName.trim();
                        if (ingredientName != '') {
                            a.add(ingredientName.toLowerCase());
                        }
                    }
                }
                a.forEach(ingredient => options.push({ value: ingredient, label: ingredient }));
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
        let nameFilterInput = document.getElementById("nameFilter") as HTMLInputElement;
        let nameFilter: string = nameFilterInput.value;
        let filteredRecipes: IRecipe[] = [...recipes];
        if (filter.length > 0) {
            (document.getElementById("preporuceno") as HTMLInputElement).disabled = true;
            (document.getElementById("prosjecno") as HTMLInputElement).disabled = true;
            (document.getElementById("popularnost") as HTMLInputElement).disabled = true;
            filteredRecipes = filteredRecipes.filter(recipe => {
                let ingredients: string[] = recipe.ingredients.map(i => i.ingredientName.toLowerCase());
                let intersection = ingredients.filter(i => filter.includes(i)).length;
                let union = filter.concat(ingredients.filter(i => !filter.includes(i))).length;
                let jaccard = intersection / union;
                recipe.jaccard = jaccard;
                return jaccard > 0.3 && recipe.title.toLowerCase().includes(nameFilter.toLowerCase());
            });
            filteredRecipes.sort((r1, r2) => {
                if (r1 && r2 && r1.jaccard && r2.jaccard) {
                    return r1.jaccard - r2.jaccard
                } else {
                    throw "Jaccard doesn't exist";
                }
            });
        } else {
            (document.getElementById("preporuceno") as HTMLInputElement).disabled = false;
            (document.getElementById("prosjecno") as HTMLInputElement).disabled = false;
            (document.getElementById("popularnost") as HTMLInputElement).disabled = false;
            filteredRecipes = recipes.filter(r => r.title.toLowerCase().includes(nameFilter.toLowerCase()));
        }
        for (const recipe of filteredRecipes) {
            const displayImage = recipe.images[0];
            const author = recipe.username;
            const src = `data:image/jpg;base64, ${displayImage.imageData}`;
            const href = `/recipes/${recipe.id}`
            const a =
                <div className="col-md-4">
                    <div className="card">
                        <img src={src} className="card-img-top" alt="..."></img>
                        <div className="card-body">
                            <h5 className="card-title">{recipe.title}</h5>
                            <h6 className="card-subtitle mb-2 text-muted">{author}</h6>
                            <p className="card-text">{recipe.recipeDescription}</p>
                            <Link to={href} className="btn mr-2">Pojedinosti o receptu</Link>
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
        for (let i = 0; i < recipes.length; i++) {
            const ratings = recipes[i].ratings.map(rating => rating.ratingValue)
            recipes[i].ocjena = ratings.reduce((a, b) => a + b, 0) / ratings.length || 0;
        }
        console.log(recipes)
        recipes.sort((a, b) => {
            if (typeof b.ocjena != "undefined" && typeof a.ocjena != "undefined") {
                return b.ocjena - a.ocjena;
            } else {
                throw "Ocjena doesn't exist";
            }
        });
        writeRecipes();
    }

    const premaPopularnosti = () => {
        recipes.sort((a, b) => b.popularity - a.popularity);
        writeRecipes();
    }

    const premaPreporuceno = async () => {
        let prosjecnaPopularnost = 0;
        let prosjecnaOcjena = 0;
        for (let i = 0; i < recipes.length; i++) {
            const ratings = recipes[i].ratings.map(rating => rating.ratingValue)
            const ocjena = ratings.reduce((a, b) => a + b, 0) / ratings.length || 0;
            recipes[i].ocjena = ocjena;
            prosjecnaOcjena += ocjena;
            prosjecnaPopularnost += recipes[i].popularity;
        }
        prosjecnaOcjena /= recipes.length;
        prosjecnaPopularnost /= recipes.length;

        recipes.sort((a, b) => {
            if (typeof b.ocjena != "undefined" && typeof a.ocjena != "undefined") {
                let razlomakA = a["ocjena"] / prosjecnaOcjena + a["popularity"] / prosjecnaPopularnost;
                let razlomakB = b["ocjena"] / prosjecnaOcjena + b["popularity"] / prosjecnaPopularnost;
                return razlomakB - razlomakA;
            } else {
                throw "Ocjena ne postoji"
            }
        });
        writeRecipes();

    }

    useEffect(() => {
        getName();
        getRecipes();
    }, [recipes, setOptions, filter]);


    return (
        <div>

            <div id="tools">
                <label>Sortiraj po:</label>
                <button id="preporuceno" className=" btn " onClick={premaPreporuceno}>Prema preporuceno</button>
                <button id="prosjecno" className=" btn  " onClick={premaProsjecnojOcjeni}>Prema prosjecnoj ocjeni</button>
                <button id="popularnost" className=" btn " onClick={premaPopularnosti}>Prema popularnosti</button>
                <input id="nameFilter" type="text" placeholder="Pretraži po imenu" onChange={e => writeRecipes()}></input>
            </div>
            <div id="filterdiv">
                <Select placeholder="Select ingredients" id="ingredient-filter" isMulti options={options} onChange={e => { setFilter(e.map(a => a.value)); writeRecipes(); }}></Select>
            </div>

            <div className="container mx-auto mt-4">
                <div className="row">
                    {content}
                </div>
            </div>
            {
                mess && (
                    <div className="form-group">
                        <div className="alert alert-danger" role="alert">
                            {mess}
                        </div>
                    </div>
                )
            }
        </div >
    )
}

export default Home;
