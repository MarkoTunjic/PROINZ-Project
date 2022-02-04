import React, {useState, SyntheticEvent, useEffect} from "react";
import { Redirect } from "react-router-dom";
import axios from "axios";
import {postPath} from "../api/api";

const Register = () => {
    const [firstName, setFirstName] = useState (''); // name is variable and setName is a fun that changes the name
    const [lastName, setLastName] = useState ('');
    const [dateOfBirth, setDateOfBirth] = useState ('');
    const [username, setUserName] = useState ('');
    const [email, setEmail] = useState ('');
    const [password1, setPassword1] = useState ('');
    const [password2, setPassword2] = useState ("");
    const [redirect, setRedirect] = useState (false);
    const [message, setMessage] = useState("");
    const [password, setPassword] = useState("");

    const submit = async (e: any) => {
        e.preventDefault();
        const registerResult = await postPath('/auth/register', {
            firstName,
            lastName,
            dateOfBirth,
            username,
            email,
            password: password1
        }, undefined);

        if (registerResult.status == 200){
            const loginResult = await postPath('/auth/login', {
                username: username,
                password: password1
            }, undefined);
            if (loginResult.status == 200) {
                localStorage.setItem("user", JSON.stringify(loginResult.data));
                setRedirect(true);
            }
        } else {
            setMessage((registerResult.data &&
                    registerResult.data.message) ||
                registerResult.message ||
                registerResult.toString);
            setPassword('');
        }
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
                <h1 className="h3 mb-3 fw-normal">Please register</h1>
                <input className="form-control" placeholder="First name" name="firstName" required
                    onChange = {e => setFirstName (e.target.value)} maxLength={25}
                />
                <input className="form-control" placeholder="Last name" name="lastName" required
                    onChange = {e => setLastName (e.target.value)} maxLength={25}
                />
                <input className="form-control" placeholder="Date of birth YYYY-MM-DD" name="dateOfBirth" required
                    onChange = {e => setDateOfBirth (e.target.value)}
                />
                <input className="form-control" placeholder="Username" name="username" required
                    onChange = {e => setUserName (e.target.value)} maxLength={20}
                />
                <input type="email" className="form-control" placeholder="name@example.com" name="email" required
                    onChange = {e => setEmail (e.target.value)} maxLength={50}
                />

                <input type="password" className="form-control" placeholder="Password" name="password" required
                    onChange = {e => setPassword1 (e.target.value)}
                />
                <input type="password" className="form-control" placeholder="Reenter password" required
                    onChange = {e => setPassword2 (e.target.value)}
                />

            <button className="w-100 btn btn-lg btn-primary submitbutton" type="submit">Submit</button>
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

export default Register;
