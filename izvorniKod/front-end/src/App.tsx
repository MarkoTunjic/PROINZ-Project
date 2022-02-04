import React, { useEffect, useState } from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import './App.css';
import Nav from './components/Nav';
import Logout from './components/Logout';
import AddRecipe from './components/AddRecipe';
import AuthorRecipes from './components/AuthorRecipes';
import EditRecipe from './components/EditRecipe';
import Recipe from "./components/Recipe";
import ModeratorViewUsers from "./components/ModeratorViewUsers";

function App() {
  const [render, setRender] = useState(true);

  useEffect(() => {
    setRender(true);
  });

  return (
    <div className="app">

      <BrowserRouter>
        <Switch>
          <Route path={[
              '/',
              '/login',
              '/register',
              '/recipes/new',
              '/recipes/:id',
              '/author/:id',
              '/author/recipe/edit/:id',
              '/moderator/users'
          ]} exact component={Nav} />
        </Switch>
        <main className="main">
          <Switch>
            <Route path='/' exact component={Home} />
            <Route path='/login' exact component={Login} />
            <Route path='/register' exact component={Register} />
            <Route path='/logout' exact component={Logout} />
            <Route path='/recipes/new' exact component={AddRecipe} />
            <Route path='/recipes/:id' exact component={Recipe} />
            <Route path='/author/recipe/edit/:id' exact component={EditRecipe} />
            <Route path='/author/:id' exact component={AuthorRecipes} />
            <Route path='/moderator/users' exact component={ModeratorViewUsers} />
          </Switch>
        </main>
      </BrowserRouter>
    </div>
  )
}

export default App;
