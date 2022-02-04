import {IUser} from "../api/models";

const MODERATOR = "MODERATOR";

export function getLoggedInUserId() {
    if(!isUserLoggedIn()){
        return null;
    }
    const userInfo = JSON.parse(localStorage.getItem("user") || '{}');
    if (userInfo)
        return userInfo["id"];
}

export function isLoggedInUserModerator() {
    if(!isUserLoggedIn()){
        return false;
    }
    const userInfo = JSON.parse(localStorage.getItem("user") || '{}');
    if (userInfo)
        return userInfo["roles"].includes(MODERATOR);
}

export function splitStringByNewlines(input: string) {
    return input.split("\n");
}

export function isUserModerator(user: IUser) {
    return user.userRole.roleName == MODERATOR;
}

export function isUserLoggedIn() {
    return !! localStorage.getItem("user");
}
