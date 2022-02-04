import React, {ChangeEvent, useEffect, useState} from "react";
import { Redirect, useParams } from "react-router-dom";
import { Button, Container, FloatingLabel, Form } from "react-bootstrap";
import { getPath, putPath } from "../api/api";
import { userInfo } from "os";

const EditRecipe = () => {
    const [recipeDescription, setRecipeDescription] = useState ('');
    const [title, setTitle] = useState('');
    const [estimatedTime, setEstimatedTime] = useState('');
    const [ingredients, setIngredients] = useState<{ name: string, quantity: number, measure: string }[]>([]);
    const [recipeSteps, setRecipeSteps] = useState<{ description: "" }[]>([]);
    const [redirect, setRedirect] = useState (false);
    const [message, setMessage] = useState('');
    const id = useParams<{id:string}>();
    const [userId,setUserId] = useState('')


    const getRecipe = async () => {
        const result = await getPath('/recipes/' + id.id, undefined);
        if (result.status == 200) {
            setRecipeDescription(result.data.recipeDescription);
            setEstimatedTime(result.data.estimatedTime);
            for(let i = 0; i < result.data["ingredients"].length; i++){
              ingredients.push({ name: result.data.ingredients[i].ingredientName, quantity: result.data.ingredients[i].ingredientQuantity, measure: result.data.ingredients[i].ingredientMeasure });
            }
            for(let i = 0; i < result.data["recipeSteps"].length; i++){
              recipeSteps.push({ description: result.data.recipeSteps[i].stepDescription});
            }
            setTitle(result.data["title"]);
        }
    }

    const config = {
      headers: {
        'content-type': 'multipart/form-data'
      }
    }

    const submit = async (e: any) => {
    e.preventDefault();

    const userLocal = localStorage.getItem("user");
    const userInfo = userLocal ? JSON.parse(userLocal) : null;
    setUserId(userInfo.id)


    let formData = new FormData();
    formData.append("title", title);
    formData.append("recipeDescription", recipeDescription);
    for (let i = 0; i < ingredients.length; i++)
      for (let key in ingredients[i])
        formData.append(key, (ingredients as any)[i][key]);
    for (let i = 0; i < recipeSteps.length; i++)
      for (let key in recipeSteps[i])
        formData.append(key, (recipeSteps as any)[i][key]);
    formData.append("estimatedTime", estimatedTime);
    formData.append("userId", userInfo.id);

    if (userInfo){
        const result = await putPath('/recipes/' + id.id, formData, config);
        console.log(ingredients)
        if (result.status == 200) {
            setRedirect(true);
        }else{
            setMessage((result.data &&
                result.data.message) ||
                result.message ||
                result.toString);
          }

    }

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



    useEffect(() => {
        getRecipe();
    }, [setTitle, setIngredients]);



    if (redirect) {
      var a = '/author/' + userId;
      return <Redirect to = {{pathname: "/author/" + userId}} />
    }

    return (
        <div>
          <Container>
            <Form onSubmit={submit}>
            <Form.Control type="text" name = "title" onChange = {e => setTitle(e.target.value)} value = {title}/>
            <FloatingLabel controlId="floatingTextarea2" label={recipeDescription}>
    <Form.Control
      as="textarea"
      placeholder="Leave a comment here"
      style={{ height: '100px' }}
      value = {recipeDescription}
      onChange = {e => setRecipeDescription (e.target.value)}
    />
  </FloatingLabel>
  <div className="new">
            {ingredients.map((element, index) =>
              (
              <div className="form-ingredient" key={index} id={"ingredient:" + index}>
                <div className="form-group">
                  <label>Sastojak:</label>
                  <input className="ingredientInput form-control" type="text" name="name"  onChange={e => handleIngredientChange(index, e)} value={element.name || ""} autoComplete = "off" />
                  <small id={"ingredientname" + index} className="form-text text-muted"></small>
                </div>

                <div className="form-group">
                  <label>Količina:</label>
                  <input type="number" min="1" className="ingredientInput form-control" name="quantity" value={element.quantity || ""} onChange={e => handleIngredientChange(index, e)} />
                  <small id={"ingredientquantity" + index} className="form-text text-muted"></small>
                </div>

                <div className="form-group">
                  <label>Mjerna jedinica:</label>
                  <input className="ingredientInput form-control" type="text" name="measure" value={element.measure || ""} onChange={e => handleIngredientChange(index, e)} />
                  <small id={"ingredientmeasure" + index} className="form-text text-muted"></small>
                </div>
                {
                  index ?
                    <button type="button" className="button remove btn" onClick={() => removeIngredientFields(index)}>Obrisi</button>
                    : null
                }
              </div>
            ))}
            <button className="button add btn" type="button" onClick={() => addIngredientFormField()}>Novi sastojak</button>
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
  <Form.Control type="number" placeholder= "Type estimated time here"  value = {estimatedTime} onChange = {e => setEstimatedTime (e.target.value)}/>
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

export default EditRecipe;

