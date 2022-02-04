import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {isLoggedInUserModerator} from "../utils/helpers";

const Nav = () => {
  const [name, setName] = useState ("");
  const [path, setPath] = useState ("");

    useEffect ( () => {
        var user = localStorage.getItem("user");
        if (user != null) {
            var newName = JSON.parse(user);
            setName(newName.username);
            setPath("/author/" + newName.id)
        }
    });

    return (
        <nav className="navbar navbar-expand-md navbar-dark bg-dark mb-4">
          <div className="container-fluid">
            <Link to = "/" className="navbar-brand">Home</Link>

            <div>
              <ul className="navbar-nav me-auto mb-2 mb-md-0">
                {name && (
                    <>
                        {
                            isLoggedInUserModerator() &&
                            <li className="nav-item">
                                <Link to = "/moderator/users" className="nav-link active">User list</Link>
                            </li>
                        }
                        <li className="nav-item">
                            <Link to = {path} className="nav-link active">My recipes</Link>
                        </li>
                        <li className="nav-item">
                            <Link to="/logout" className="nav-link active">Logout</Link>
                        </li>
                    </>
                )}
                {!name && (
                  <li className="nav-item">
                    <Link to= "/login" className="nav-link active">Login</Link>
                  </li>
                )}
                {!name && (
                  <li className="nav-item">
                    <Link to = "/register" className="nav-link active">Register</Link>
                  </li>
                )}
              </ul>
            </div>
          </div>
        </nav>
    )
}

export default Nav;
