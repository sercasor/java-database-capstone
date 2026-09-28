import { getDoctors } from './services/doctorServices.js';
import { openModal } from './components/modals.js';
import { createDoctorCard } from './components/doctorCard.js';
import { filterDoctors } from './services/doctorServices.js';
import { saveDoctor } from './services/doctorServices.js';

/*----------------------Listeners----------------------*/
document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
    // openModal es synchronous
    document.getElementById('saveDoctorBtn').addEventListener('click', adminAddDoctor);
});

document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();
});

//listeners for search and filter logic
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);


/*----------------------Functions----------------------*/

/**
 * Calls the API to get a Doctor Array contained in a JS object, then creates all doctor cards and injects them in the dynamic section labeled with id="content"
 */
function loadDoctorCards() {
    //receives JS object that contains all Doctors in a Doctor array. then() is a promise since getDoctors returns a Promise via fetch()
    getDoctors()
        .then(doctors => {
            //clear content section to fill with the doctor list fetched from the API. This dynamic section can be found in adminDashboard.html template
            const contentDiv = document.getElementById("content");
            contentDiv.innerHTML = "";

            doctors.forEach(doctor => {
                const card = createDoctorCard(doctor);
                contentDiv.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Failed to load doctors:", error);
        });
}

/**
 * Gathers current filter/search values
 * Fetches and displays filtered results using filterDoctors() from '/services/doctorServices.js'.
 *
 */
function filterDoctorsOnChange() {
    //input elements
    const searchBar = document.getElementById("searchBar").value.trim(); //source: docu. - trim() removes whitespace from both ends of this string and returns a new string, without modifying the original string.
    const filterTime = document.getElementById("filterTime").value;
    const filterSpecialty = document.getElementById("filterSpecialty").value;

    //input data length validation
    const name = searchBar.length > 0 ? searchBar : null;
    const time = filterTime.length > 0 ? filterTime : null;
    const specialty = filterSpecialty.length > 0 ? filterSpecialty : null;

    //returns a Promise, this the then(). Similar to getDoctors() so Doctor array is fetched, iterated and respective doctor cards are created and injected in dynamic section
    filterDoctors(name, time, specialty)
        .then(response => {
            const doctors = response.doctors;
            const contentDiv = document.getElementById("content");
            contentDiv.innerHTML = "";

            if (doctors.length > 0) {
                console.log(doctors);
                doctors.forEach(doctor => {
                    const card = createDoctorCard(doctor);
                    contentDiv.appendChild(card);
                });
            } else {
                contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
                console.log("Nothing");
            }
        })
        .catch(error => {
            console.error("Failed to filter doctors:", error);
            alert("❌ An error occurred while filtering doctors.");
        });
}


/**
 * Collects the "Add Doctor" modal form data, validates it and saves the doctor via saveDoctor().
 */
async function adminAddDoctor() {
    // form input data retrieval
    const name = document.getElementById("doctorName").value.trim();
    const specialty = document.getElementById("specialization").value;
    const email = document.getElementById("doctorEmail").value.trim();
    const password = document.getElementById("doctorPassword").value;
    const phone = document.getElementById("doctorPhone").value.trim();



    // NodeList of marked checkboxes  -> array with their values ("09:00-10:00", ...)
    const availableTimes = Array.from(
        document.querySelectorAll('input[name="availability"]:checked')
    ).map(checkbox => checkbox.value);

    // input validation, availableTimes needs none since it's a checkbox input with fixed values
    if (!name || name.length<=0 || !specialty || specialty.length<=0 || !email
        || email.length<=0 || !password || password.length<=0 || !phone || phone.length<=0) {
        alert("Please fill in all the fields.");
        return;
    }

    // admin Token validation before attempting to save the doctor
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Admin session not found. Please log in again.");
        return;
    }


    const doctor = { name, specialty, email, password, phone, availableTimes };
    // calls the service (needs a Doctor object with its attributes), returns { success, message } in doctorServices.js
    const result = await saveDoctor(doctor, token);

    //  Results
    if (result.success) {
        alert(result.message || "Doctor added successfully.");
        document.getElementById("modal").classList.remove("active"); // closes  modal
        loadDoctorCards(); //  refreshes doctor list
    } else {
        alert(result.message || "Error when saving doctor.");
    }
}

