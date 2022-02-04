import React, { ChangeEvent, FormEvent, useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import axios from "axios";
import { Button, Container, FloatingLabel, Form, FormGroup } from "react-bootstrap";
import { postPath } from "../api/api";

const AddRecipe = () => {
  const [ingredients, setIngredients] = useState([{ name: "", quantity: 0, measure: "" }]);
  const [recipeSteps, setRecipeSteps] = useState([{ description: "" }]);
  const [pictures, setPictures] = useState([]);
  const [recipeDescription, setRecipeDescription] = useState('');
  const [message, setMessage] = useState('');
  const [title, setTitle] = useState('');
  const [estimatedTime, setEstimatedTime] = useState('');
  const [redirect, setRedirect] = useState(false);


  const submit = async (event: any) => {
    event.preventDefault();
    let input = document.getElementById("images") as HTMLInputElement;

    if (input.files!.length > 5) {
      alert(`Cannot upload files more than 5`);
      return;
    }

    const userLocal = localStorage.getItem("user");
    const userInfo = userLocal ? JSON.parse(userLocal) : null;
    const config = {
      headers: {
        'content-type': 'multipart/form-data'
      }
    }
    let formData = new FormData();
    formData.append("title", title);
    formData.append("recipeDescription", recipeDescription);
    for (let i = 0; i < ingredients.length; i++)
      for (let key in ingredients[i])
        formData.append(key, (ingredients as any)[i][key]);
    for (let i = 0; i < recipeSteps.length; i++)
      for (let key in recipeSteps[i])
        formData.append(key, (recipeSteps as any)[i][key]);
    for (let i = 0; i < pictures.length; i++)
      formData.append("pictures", pictures[i]);
    formData.append("estimatedTime", estimatedTime);
    formData.append("userId", userInfo.id);
    const result = await postPath('/recipes/', formData, config)

    if (result.status == 200) {
      setRedirect(true);
    } else {
      setMessage((result.data &&
        result.data.message) ||
        result.message ||
        result.toString);
    }

  }

  if (redirect) {
    return <Redirect to="/" />
  }

  let handleIngredientChange = (index: number, element: ChangeEvent<HTMLInputElement>) => {
    let newIngredientValues = [...ingredients];
    (newIngredientValues as any)[index][element.target.name] = element.target.value;
    setIngredients(newIngredientValues);
  }

  let removeIngredientFields = (index: number) => {
    let newIngredientValues = [...ingredients];
    newIngredientValues.splice(index, 1);
    setIngredients(newIngredientValues)
  }

  let addIngredientFormField = () => {
    setIngredients([...ingredients, { name: "", quantity: 0, measure: "" }])
  }

  let handleStepChange = (index: number, element: ChangeEvent<HTMLTextAreaElement>) => {
    let newStepValues = [...recipeSteps];
    (newStepValues as any)[index][element.target.name] = element.target.value;
    setRecipeSteps(newStepValues);
  }

  let removeStepFields = (index: number) => {
    let newStepValues = [...recipeSteps];
    newStepValues.splice(index, 1);
    setRecipeSteps(newStepValues)
  }

  let addStepFormField = () => {
    setRecipeSteps([...recipeSteps, { description: "" }])
  }

  const MAX_LENGTH = 5;

  const uploadMultipleFiles = (e: any) => {
    if (Array.from(e.target.files).length > MAX_LENGTH) {
      e.preventDefault();
      alert(`Cannot upload files more than ${MAX_LENGTH}`);
      return;
    } else {
      setPictures(e.target.files)
    }
  }


  return (
    <div id="recipe-add">
      <Container className="centered-container">
        <Form onSubmit={e => submit(e)} className="form">
          <Form.Control type="text" placeholder="Unesite naslov recepta" maxLength={50} onChange={e => setTitle(e.target.value)} />
          <FloatingLabel controlId="floatingTextarea2" label="Napišite ukratko opis recepta">
            <Form.Control
              as="textarea"
              placeholder="Leave a comment here"
              style={{ height: '100px' }}
              maxLength={500}
              onChange={e => setRecipeDescription(e.target.value)}
            />
          </FloatingLabel>
          <div className="new">
            {ingredients.map((element, index) => (
              <div className="form-ingredient" key={index} id={"ingredient:" + index}>
                <div className="form-group">
                  <label>Sastojak:</label>
                  <input className="ingredientInput form-control" type="text" name="name" maxLength={25} value={element.name || ""} onChange={e => handleIngredientChange(index, e)} />
                  <small id={"ingredientname" + index} className="form-text text-muted"></small>
                </div>

                <div className="form-group">
                  <label>Količina:</label>
                  <input type="number" min="1" className="ingredientInput form-control" name="quantity" value={element.quantity || ""} onChange={e => handleIngredientChange(index, e)} />
                  <small id={"ingredientquantity" + index} className="form-text text-muted"></small>
                </div>
                <div className="form-group">
                  <label>Mjerna jedinica:</label>
                  <input className="ingredientInput form-control" type="text" name="measure" maxLength={25} value={element.measure || ""} onChange={e => handleIngredientChange(index, e)} />
                  <small id={"ingredientmeasure" + index} className="form-text text-muted"></small>
                </div>
                {
                  index ?
                    <button type="button" className="button remove btn" onClick={() => removeIngredientFields(index)}>Obrisi</button>
                    : null
                }
              </div>
            ))}
            <div>
            <button className="button add btn" type="button" onClick={() => addIngredientFormField()}>Novi sastojak</button>
            </div>

          </div>
          <div className="new">
            {recipeSteps.map((element, index) => (
              <div className="form-step" key={index}>
                <div className="form-group">
                  <label>{index + 1}. korak:</label>
                  <textarea className="form-control" name="description" value={element.description || ""} onChange={e => handleStepChange(index, e)} />
                </div>

                {
                  index ?
                    <button type="button" className="button remove btn" onClick={() => removeStepFields(index)}>Obrisi</button>
                    : null
                }
              </div>
            ))}
            <button className="button add btn" type="button" onClick={() => addStepFormField()}>Novi korak</button>
          </div>
          <Form.Group controlId="formFileMultiple" className="mb-3">
            <Form.Control id="images" type="file" accept=".jpg,.png,.jpeg" multiple onChange={uploadMultipleFiles} />
          </Form.Group>
          <Form.Control type="number" placeholder="Unesite procijenjenoo vrijeme pripreme" onChange={e => setEstimatedTime(e.target.value)} />
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
      </Container>
      {message && (
        <div className="form-group">
          <div className="alert alert-danger" role="alert">
            {message}
          </div>
        </div>
      )}
    </div>
  )
}

export default AddRecipe;
