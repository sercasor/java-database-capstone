import {openModal} from "../components/modals"
import {API_BASE_URL} from "../config/config"
//localhost:8080 basically

//Admin login endpoint
const ADMIN_API = API_BASE_URL + '/admin';
//Doctor login endpoint
const DOCTOR_API = API_BASE_URL + '/doctor/login'

//add listener to button depending on the type of login so the appropiate modal is opened upon click
window.onload = function () { //onload is used to ensure the script runs after the page is fully loaded
    const adminBtn = document.getElementById('adminLogin');
    const doctorBtn = document.getElementById('doctorLogin');
    if (adminBtn) {
        adminBtn.addEventListener('click', () => {
            openModal('adminLogin');
        });
    }else if(doctorBtn){
        doctorBtn.addEventListener('click', () => {
            openModal('doctorLogin');
        });
    }
}
/*------------------------------------------GLOBALLY ACCESSIBLE FUNCTIONS VIA DECLARING THEM ON THE WINDOW OBJECT------------------------------------------*/
/**
 * Important to note: to make a function globally,  We can  Explicitly Declare on the window Object. The implicit way is  declaring it as a variable without "var" keyword. e.g: myFunction= function(){}
 * Source: https://sqlpey.com/javascript/javascript-function-scope-accessibility/
 * Source: https://www.w3schools.com/Js/js_async.asp
 * The word async means not at the same time.
 *
 * Instead of waiting for one task to finish before starting the next, JavaScript can continue running other code while waiting for an operation to complete.
 *
 * When the operation finishes, JavaScript continues by running code that handles the result.
 */
window.adminLoginHandler= async function (){
    const username=document.getElementById("username").value;
    const password=document.getElementById("password").value;

    const admin = { username, password };

    if(!username||!password){

        return console.log("Username or password is missing");

        //TODO: aqui habria que hacer una combinacion de await con fetch  para conseguir la respuesta???
    }

        try {
            //await operator is used to wait for a Promise and get its fulfillment value.
            const response = await fetch(ADMIN_API, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(admin)
            });

            if (response.ok) {
                const data = await response.json(); // extracts the body as JSON
                localStorage.setItem("token", data.token);
                selectRole("admin"); //saves the role
            } else {
                alert("Invalid credentials!");
            }


        }catch (e) {
            console.error(`Invalid credentials! Exception:  ${e}`)
        }






}

window.doctorLoginHandler= async function (){


    const email=document.getElementById("email").value;
    const password=document.getElementById("password").value;

    const doctor = { email, password };

    if(!email||!password){

        return console.log("email or password is missing");

        //TODO: aqui habria que hacer una combinacion de await con fetch  para conseguir la respuesta???
    }

    try {
        //await operator is used to wait for a Promise and get its fulfillment value.
        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctor)
        });

        if (response.ok) {
            const data = await response.json(); // extracts the body as JSON
            localStorage.setItem("token", data.token);
            selectRole("doctor"); //saves the role
        } else {
            let errorMessage="Invalid credentials!";
            alert(errorMessage);
        }


    }catch (e) {
        console.error(`Invalid credentials! Exception:  ${e}`)
    }






}