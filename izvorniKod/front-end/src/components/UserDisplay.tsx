import {IUser, IUserDisplay, RecipeStep, RecipeStepDisplayInterface} from "../api/models";
import {deletePath} from "../api/api";


const UserDisplay = (props: IUserDisplay) => {
    const handleUserDelete = () => {
        // eslint-disable-next-line no-restricted-globals
        const out = confirm("Jeste li sigurni da želite izbrisati korisnika'" + props.firstName + " " + props.lastName + "'")
        if (out)
            deletePath('/users/' + props.id)
    }
    return (
        <div className="user-display">
            <p>Ime: {props.firstName}</p>
            <p>Prezime: {props.lastName}</p>
            <p>Korisničko ime: {props.username}</p>
            <p>Email: {props.email}</p>
            <p>Datum rođenja (YYYY-MM-DD): {props.dateOfBirth}</p>
            <button className="btn"
                    onClick={handleUserDelete}>
                Izbriši korisnika
            </button>
        </div>
    );
}

export default UserDisplay;
