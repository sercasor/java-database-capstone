# Frontend

**IMPORTANT** patientDashboard.html seems to be the original template so if there are issues, we can follow that


create addEventListeners for index.html role selection buttons
create addEventListeners for index.html modal section

<!--TODO: DEBUG IF THIS CLASS IS ADEQUATELY CONFIGURED
*Modal and Button Interactions:

    Modal and button interaction styles include close button (.close), hover effects, and modal positioning.

-->
button.close{
    display: none;
}

Con respecto al CSS del header/nav: Si tienes roles distintos (Admin/Doctor/Patient) mostrando distintos botones de nav dinámicamente vía Thymeleaf (th:if), asegúrate de que el gap y flex-wrap sigan funcionando bien independientemente de cuántos ítems se rendericen.

##adminDashboard.html
Interactive Elements:

    Style the search bar, filter dropdowns, and the Add Doctor button:

Modal Styling:

    Ensure the modal is centered, hidden by default, and has smooth transitions
    Style inputs inside the modal form with padding and focus effects

#global
find a substitute for any <body onload="renderContent()">. Some examples are patientDashboard. I reckon all dashboards share a similar situation


#header.js
desarrollar openModal()??

desarrollar las funciones y listeners para 
   case "patient":
            headerContent += `
           <button id="patientLogin" class="adminBtn">Login</button>
           <button id="patientSignup" class="adminBtn">Sign Up</button>`;
            break;
#doctorCard.js

finish this. To do that, research how alerts work (a conditional will be required), call the function that will be declared in the service (fix the import if need be)
