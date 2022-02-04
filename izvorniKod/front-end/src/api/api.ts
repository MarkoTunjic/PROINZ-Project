import axios, {AxiosRequestConfig} from "axios";
import {API_SUFFIX, BASE_API_LOCAL_PATH, HTTP_PREFIX, LOCALHOST} from "../config/constants";

const hostname = window.location.hostname;
const baseApiPath = hostname == LOCALHOST ? BASE_API_LOCAL_PATH : HTTP_PREFIX + hostname + API_SUFFIX

export async function postPath(appendPath: string, payload: {} | undefined, axiosConfig: AxiosRequestConfig<any> | undefined){
    setAccessToken();
    const path = baseApiPath + appendPath;

    console.info("Posting: " + path);
    const response = await axios.post(path, payload, axiosConfig)
         .then((axiosResponse) => {
             return axiosResponse;
         }).catch((error) => {
             return error.response || error.message || error.toJSON();
         })
    return response;
}

export async function getPath(appendPath: string, axiosConfig: AxiosRequestConfig<any> | undefined){
    setAccessToken();
    const path = baseApiPath + appendPath;

    console.info("Getting: " + path);
    const response = await axios.get(path, axiosConfig)
        .then((axiosResponse) => {
            return axiosResponse;
        }).catch((error) => {
            return error.response || error.message || error.toJSON();
        })
    return response;
}


export async function putPath(appendPath: string, payload: {} | undefined, axiosConfig: AxiosRequestConfig<any> | undefined){
    setAccessToken();
    const path = baseApiPath + appendPath;

    console.info("Putting: " + path);
    const response = await axios.put(path, payload, axiosConfig)
         .then((axiosResponse) => {
             return axiosResponse;
         }).catch((error) => {
             return error.response || error.message || error.toJSON();
         })
    return response;
}

export async function deletePath(appendPath: string, axiosConfig: (AxiosRequestConfig<any> | undefined) = undefined) {
    setAccessToken();
    const path = baseApiPath + appendPath;

    console.info("Deleting: " + path);
    const response = await axios.delete(path, axiosConfig)
        .then((axiosResponse) => {
            return axiosResponse;
        }).catch((error) => {
            return error.response || error.message || error.toJSON();
        })
    return response;
}
function setAccessToken(){
    const userLocal = localStorage.getItem("user");
    const userInfo = userLocal ? JSON.parse(userLocal) : null;
    if (userInfo){
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + userInfo.acessToken;
    }
}
