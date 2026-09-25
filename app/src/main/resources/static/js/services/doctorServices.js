
import {API_BASE_URL} from "../config/config"
//localhost:8080 basically

//Doctor login endpoint
const DOCTOR_API = API_BASE_URL + '/doctor'

export async function getDoctors() {
    let doctors=[];
    try {
        //await operator is used to wait for a Promise and get its fulfillment value.
        const response = await fetch(DOCTOR_API, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {

            const data = await response.json(); // extracts the body and returns a JS object
            doctors=data.doctors;
        } else {
            console.error("No doctors found!");

        }


    }catch (e) {
        console.error(`Error when fetching doctors list:  ${e}`)
    }
    return doctors;
}

//deletes a specific doctor using their ID and an authentication token (for security)
export async function deleteDoctor(doctorID, token) {
    const URL = DOCTOR_API + `/${doctorID}/${token}`;
    try {
        const response = await fetch(URL,
            {
                method: 'DELETE'
            }); //header is not needed as no info is sent
        const data = await response.json(); // extracts  JSON always
        return {
            success: response.ok,
            message: data.message };
    } catch (e) {
        console.error(`Error when deleting doctor: ${e}`);
        return {
            success: false,
            message: "Error attempting doctor removal." };
    }
}
//this function powers the “Add Doctor” modal in the Admin dashboard and saves new doctor records in the database
export async function saveDoctor(doctor, token) {
    const URL = DOCTOR_API + `/${token}`;
    try {
        const response = await fetch(URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctor) //
        });
        const data = await response.json();
        return { success: response.ok, message: data.message };
    } catch (e) {
        console.error(`Error when saving doctor: ${e}`);
        return {
            success: false,
            message: "Error when saving doctor." };
    }
}
//this function supports real-time search and filter features in the Admin dashboard
export async function filterDoctors(name, time, specialty) {
    const URL = DOCTOR_API + `/filter/${name}/${time}/${specialty}`;
    let doctors=[];
    try {
        const response = await fetch(URL,
            {
                method: 'GET'
            });

        if (response.ok) {
            const data = await response.json();
            doctors=data.doctors; //  TODO: data.doctors is needed instead of data due to  backend response in the form of {doctors:[]}

        } else {
            console.error("Filter error!");

        }
    } catch (e) {
        console.error(`Error when filtering doctors: ${e}`);

    }
    return doctors;
}