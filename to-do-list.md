#Module 2: database designs and JPA models

##Lab: Building Models with Validations
Next steps

Now that you have coded your models, you are ready to work on the rest of the code base. We encourage you to extend your model so your submission stands out.

Here are a few ideas to enhance your models further. This is completely optional and if you decide to add these models, you will also need to extend the remaining of the project to accomodate for this change.

    Add additional fields for more detailed information:
        For Doctor: Add yearsOfExperience, clinicAddress, or rating
        For Patient: Add dateOfBirth, emergencyContact, or insuranceProvider
        For Appointment: Add reasonForVisit or notes
        For Prescription: Add refillCount or pharmacyName

    Apply more advanced validations to ensure better data quality:
        Use @Pattern to validate phone numbers with a specific format
        Use @Min and @Max for fields like yearsOfExperience or rating
        Use @Past for dateOfBirth to ensure dates are in the past
        Limit string lengths using @Size(min, max) to avoid unexpected inputs

    Enhance JSON handling:
        Use @JsonIgnore to hide internal fields you don't want exposed in API responses
        Customize field names in JSON using @JsonProperty("customName")

By thoughtfully extending your models, you not only make your application more realistic but also demonstrate deeper knowledge of JPA, Hibernate, and MongoDB best practices. 

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

##Lab: Creating Frontend Pages


##Lab: Developing Services and Utilities
Extend Filters: Add dropdowns or checkboxes to filter patients by status ("Consulted" and "pending").
Error Handling: Display user-friendly messages when API calls fail or return unexpected data.
Mobile Optimization: Use media queries to fine-tune layouts for phones and tablets.
Add pagination or infinite scroll to handle large patient lists.

#MVC
  Implement authentication & authorization:
        Explore Spring Security to add real authentication (JWT, OAuth, etc.) to further secure your app.
        Use roles and permissions to restrict access to specific resources based on user credentials.

    Enhance token validation:
        Build a more robust token validation system (e.g., JWT token expiration handling).
        Add error handling to provide user-friendly messages when token validation fails.

#endpoints
Next Steps

Now that you have coded this, you can take the following steps to continue improving and extending your submission:

    Implement authentication and authorization using Spring Security.
    Protect API endpoints based on user roles such as admin, doctor, or patient.
    Store and verify passwords securely using hashing algorithms.
    Apply role-based access control (RBAC) to secure sensitive operations.

These enhancements will make your application production-ready by ensuring only authorized users can access specific features.
