import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import axios from "axios";

const userLocal = localStorage.getItem("user");
const userInfo = userLocal ? JSON.parse(userLocal) : null;
if (userInfo){
    axios.defaults.headers.common['Authorization'] = userInfo.acessToken;
}

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

