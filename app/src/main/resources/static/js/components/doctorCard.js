/*
*
* Import specifies the function and the module it belongs to
* doc: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/import
* */
import {showBookingOverlay} from "../loggedPatient.js";
import {deleteDoctor} from "../services/doctorServices.js";
import {getPatientData} from "../services/patientServices";

/**
 * Function to create Doctor cards that contain their information. A Doctor object is used as parameter.
 * export is used alongside the function declaration so other files can import the function
 */

export function createDoctorCard(doctor){

    //creates the container and adds styles
    const card = document.createElement("div");
    card.classList.add("doctor-card");


    const role = localStorage.getItem("userRole"); //the role will be used to decide which button should be shown

    //Doctor Info Section with its styles. Contains name, specialty, email, and availability.
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    //Doctor attributes
    const name = document.createElement("h3");
    name.textContent = doctor.name;
    const specialty = document.createElement("h3");
    specialty.textContent = doctor.specialty;
    const email = document.createElement("h3");
    email.textContent = doctor.email;
    const availability = document.createElement("h3");
    availability.textContent = doctor.availability;

    infoDiv.appendChild(name);
    infoDiv.appendChild(specialty);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    //to hold buttons like “Delete” or “Book Now”
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    /*--------------------Conditionals-------------------*/
    if (role === "admin") {
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.addEventListener("click", async () => { //TODO: FINISH THIS FUNCTION
            // 1. Confirm deletion
            // 2. Get token from localStorage
            const token = localStorage.getItem("token");
            // 3. Call API to delete
            deleteDoctor(doctor.id); //DEBUG: MIGHT NEED DIFFERENT PARAMETERS
            // 4. On success: remove the card from the DOM
        });
    }
    else if (role === "patient") { //not logged-in
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.addEventListener("click", () => {
            alert("Patient needs to login first.");
        });
    }
    else if (role === "loggedPatient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.addEventListener("click", async (e) => {
            const token = localStorage.getItem("token");
            const patientData = await getPatientData(token);
            showBookingOverlay(e, doctor, patientData);
        });
    }

    //the card is finally assembled after taking into consideration the conditionals. The 2 components are the doctor personal information and the available buttons depending on userRole
    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);
    return card;
}

