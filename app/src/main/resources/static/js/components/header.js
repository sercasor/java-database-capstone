
function renderHeader(){
    const headerDiv =document.getElementById("header");


    //check current page so the role-based header is not shown on the home
    if (window.location.pathname.endsWith("/")) {
        //localStorage is similar to sessionStorage  ,
        // except that while localStorage data has no expiration time, sessionStorage data gets cleared when the page session ends
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");

        //returns just the logo and site title as header
        headerDiv.innerHTML = `
           <header class="header">
             <div class="logo-section">
               <img src="/assets/images/logo/logo.png" alt="CMS Logo" class="logo-img">
               <span class="logo-title">Hospital CMS</span>
             </div>
           </header>`;
        return;
    }

    //Looks at the user’s role and login(auth) token in localStorage ,to determine which header layout to show
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");



    //initialize header content before adding specific content depending on the role
    let headerContent = `<header class="header">
         <div class="logo-section">
           <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
           <span class="logo-title">Hospital CMS</span>
         </div>
         <nav>`;

    //check invalid handle button, ends sessions and redirects home
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    //Injects the appropriate header HTML into the variables (needs reassigning variable valuable via headerDiv.innerHTML=yaddayadda;
    switch(role) {
        case "admin":
            headerContent += `
           <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
           <a href="#" onclick="logout()">Logout</a>`;
            break;
        case "doctor":
            headerContent += `
           <button class="adminBtn"  onclick="selectRole('doctor')">Home</button>
           <a href="#" onclick="logout()">Logout</a>`;
            break;
        case "patient":
            headerContent += `
           <button id="patientLogin" class="adminBtn">Login</button>
           <button id="patientSignup" class="adminBtn">Sign Up</button>`;
            break;
        case "loggedPatient":
            headerContent += `
           <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
           <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
           <a href="#" onclick="logoutPatient()">Logout</a>`;
            break;
    }


    //inserts HTML code and listeners
    headerDiv.innerHTML = headerContent;
    attachHeaderButtonListeners();

    //TODO: seguimos en "Attach Event Listeners because elements were dynamically created, you need to attach listeners after insertion." de las instrucciones

}
