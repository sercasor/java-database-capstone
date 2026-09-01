A.- Architecture summary

Project to showcase Java Springboot skills using MVC pattern , REST API, Spring Data JPA (MySQL) and MongoDB . Project structured in 3 layers: UI, APP and Data.
Thymeleaf templates are used for the Admin and Doctor dashboards (view layer) whereas REST API serves all modules.  On the one hand, MongoDB (via Document models) is used for prescriptions on the other hand MySQL (via JPA Entities) stores all patient, doctor, appointment, and admin data.
Requests are routed via a common service layer, which delegates to the appropiate repository.

B.- Data flow and control

1-User is authenticated and redirected to either AdminDashboard or Appointment pages depending on their role
2-The appropiate Thymeleaf template or REST controller is called
3- The controller calls the Service layer
4- The Service Layer communicates with the Repository Layer to perform data access operations
5- The MySQL repositories are accessed for requests related to Patients, Doctors, appointments or Admins. If prescription data is enquired the MongoDB repository is accessed instead
6-Each repository accesses the corresponding database depending on whether the data is normalized and relational (MySQL) or nested and flexible (MongoDB)
7- MySQL DB models are turned into JPA Entities whereas MongoDB ones become Document Models
8- MVC flows: models are passed from the controller to Thymeleaf templates, which are rendered (server-side rendering) as dynamic HTML for the browser. In contrast, REST flows entail model serialization into JSON and its transmission as part of an HTTP response.
