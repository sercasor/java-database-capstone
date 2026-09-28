import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

/*----------------------Global variables----------------------*/
const tableBody = document.getElementById("patientTableBody"); //table body where rows will be rendered
const token = localStorage.getItem("token");
let selectedDate = getTodayString(); // 'YYYY-MM-DD'
let patientName = null;              // filter by patient name

/*----------------------Helpers----------------------*/

/**
 * Returns today's date as 'YYYY-MM-DD' using the LOCAL timezone.
 * (toISOString() uses UTC and can return yesterday/tomorrow near midnight.)
 */
function getTodayString() {
    return new Date().toLocaleDateString("en-CA"); // the en-CA locale formats as YYYY-MM-DD
}

/**
 * Renders a single full-width row with a message inside the table body. Used to display errors and such
 */
function showMessageRow(message) {
    tableBody.innerHTML = "";
    const row = document.createElement("tr");
    const cell = document.createElement("td");
    cell.colSpan = 5; //  the number of columns in my <thead>
    cell.textContent = message;
    cell.style.textAlign = "center";
    row.appendChild(cell);
    tableBody.appendChild(row);
}

/*----------------------Listeners----------------------*/

// Search bar: filter by patient name. Important: "input" is any event related to an <input>. QUoting Mozilla: "The input event fires when the value of an <input>, <select>,
// or <textarea> element has been changed as a direct result of a user action (such as typing in a textbox or checking a checkbox)."
document.getElementById("searchBar").addEventListener("input", (event) => {
    const value = event.target.value.trim();
    patientName = value.length > 0 ? value : "null"; // backend expects the literal string "null"
    loadAppointments();
});

// "Today's Appointments" button
document.getElementById("todayButton").addEventListener("click", () => {
    selectedDate = getTodayString();
    document.getElementById("datePicker").value = selectedDate; // sync the UI
    loadAppointments();
});

// Date picker
document.getElementById("datePicker").addEventListener("change", (event) => {
    selectedDate = event.target.value;
    loadAppointments();
});

/*----------------------Functions----------------------*/

/**
 * Fetches the appointments for selectedDate / patientName and renders them as table rows.
 */
async function loadAppointments() {
    try {
        const response = await getAllAppointments(selectedDate, patientName, token); //calls the API
        // Supports both shapes: { appointments: [...] } or directly [...] depending on the API response
        let appointments;
        if (Array.isArray(response)) {
            appointments = response; //formatted as [appointment1,etc.]
        } else {
            // ?.  is optional chaining. Tries to read appointments property but if null/undefined, it returns undefined.
            // ?? returns the property on the right (an empty array if the one on the left is null/undefined
            appointments = response?.appointments ?? []; //formated as {appointments:[]} if true so appointments will contain anarray regardless of API response
        }

        tableBody.innerHTML = "";

        if (appointments.length === 0) {
            showMessageRow("No Appointments found for today.");
            return;
        }
        //note: appointments are DTOs (checkout DTO folder inside backend's)
        appointments.forEach(appointment => {
            const patient = {
                id: appointment.patientId,
                name: appointment.patientName,
                phone: appointment.patientPhone,
                email: appointment.patientEmail

            };

            const appointmentID=appointment.id;
            const doctorId=appointment.doctorId;
            const row = createPatientRow(patient, appointmentID, doctorId); //returns a <tr> as an HTML table row (a dynamic section)
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Error loading appointments:", error);
        showMessageRow("Error loading appointments. Try again later.");
    }
}

/*----------------------Initial render----------------------*/
document.addEventListener("DOMContentLoaded", () => {
    // renderContent() lives in render.js (loaded as a classic script, so it's global)
    if (typeof renderContent === "function") {
        renderContent();
    }
    document.getElementById("datePicker").value = selectedDate; // show today in the picker
    loadAppointments();
});