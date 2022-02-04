import UserDisplay from "./UserDisplay";
import {getLoggedInUserId, isLoggedInUserModerator} from "../utils/helpers";
import {useEffect, useState} from "react";
import {IComment, IRecipe, IUser} from "../api/models";
import {getPath} from "../api/api";


const ModeratorViewUsers = () => {
    const [users, setUsers] = useState<IUser[]>([]);

    const getAllUsers = async () => {
        const result = await getPath(`/users/`, undefined);
        if (result.status == 200) {
            return result.data as IUser[];
        } else {
            console.error("Error when getting users");
        }
    }

    useEffect(() => {
        getAllUsers().then((userList) => {
                if (userList) {
                    setUsers(userList)
                }
            }
        )
    }, [])

    return (
        <>
            {
                isLoggedInUserModerator() &&
                    <div className="user-list">
                        { users.sort((a,b) => {return a.id - b.id}).map(user => {
                            if(getLoggedInUserId() == user.id){
                                return <></>;
                            }
                            return <UserDisplay id={user.id}
                                               dateOfBirth={user.dateOfBirth}
                                               email={user.email}
                                               firstName={user.firstName}
                                               lastName={user.lastName}
                                               username={user.username}
                            />
                        })
                        }
                    </div>
            }
        </>
    );
}

export default ModeratorViewUsers;
