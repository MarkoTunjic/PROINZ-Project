import axios from "axios";
import React, { useEffect, useState } from "react";
import { Redirect } from "react-router";
import { json } from "stream/consumers";

const Logout = () => {

    useEffect ( () => {
        localStorage.removeItem("user");
    });
    
    return <Redirect to = "/"/>
    
}

export default Logout;