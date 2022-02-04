import axios from "axios";
import React, { useEffect, useState } from "react";
import { BrowserRouter, Redirect } from "react-router-dom";
import Nav from "./Nav";
import {postPath} from "../api/api";

const Login = () => {
  const [message, setMessage] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [redirect, setRedirect] = useState(false);

  const submit = async (e: any) => {
    e.preventDefault();
    const result = await postPath('/auth/login', {
      username, password
    }, undefined)
    if (result.status == 200) {
      localStorage.setItem("user", JSON.stringify(result.data));
      console.log(localStorage)
      axios.defaults.headers.common['Authorization'] = 'Bearer:' + result.data.acessToken;
      setRedirect(true);
    } else {
      setMessage((result.data &&
          result.data.message) ||
          result.message ||
          result.toString);
      setPassword('');
    }
    /*
    axios.post("http://localhost:8080/api/auth/login", {
      username,
      password
    }).then(response => {
      localStorage.setItem("user", JSON.stringify(response.data));
      console.log(localStorage.getItem("user"));
      setRedirect(true);
    }, error => {
      setMessage((error.response &&
        error.response.data &&
        error.response.data.message) ||
        error.message ||
        error.toString());
      setPassword("");
    })*/
  }


  const [name, setName] = useState("");

  useEffect(() => {
    var user = localStorage.getItem("user");
    if (user) {
      var newName = JSON.parse(user);
      setName(newName.username);
    }
  });

  if (redirect || name) {
    return <Redirect to="/" />
  }

  return (
    <div>
      <form className="form-signin" onSubmit={submit}>
        <h1 className="h3 mb-3 fw-normal">Please sign in</h1>

        <input type="username" className="form-control" placeholder="Username"
          onChange={e => setUsername(e.target.value)} value={username}
        />

        <input type="password" className="form-control" placeholder="Password"
          onChange={e => setPassword(e.target.value)} value={password}
        />

        <button className="w-100 btn btn-lg btn-primary submitbutton" type="submit">Sign in</button>
      </form>
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

export default Login;
