# Smart Clinic Managemenrt System

# Deployment

Prerequisites: the following programs must be installed:
- Docker
- Docker compose
- JDK 17 or higher
- Maven

Copy the contents of .env.example to .env and replace the values with your own before executing  docker compose up. To do that, change directory to app/docker. When the containers are up, change directory again to app/ and execute mvn spring-boot:run. 

#MYSQL PROCEDURES

some procedures are used so you'll have to connect to MySQL CLI to create them. These are the procedures:

DELIMITER $$

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN report_date DATE
)
BEGIN
    SELECT 
        d.name AS doctor_name,
        a.appointment_time,
        a.status,
        p.name AS patient_name,
        p.phone AS patient_phone
    FROM 
        appointment a
    JOIN 
        doctor d ON a.doctor_id = d.id
    JOIN 
        patient p ON a.patient_id = p.id
    WHERE 
        DATE(a.appointment_time) = report_date
    ORDER BY 
        d.name, a.appointment_time;
END$$

DELIMITER ;


<!--here's a sample call to test if the procedure is working. You may need to modify the date-->

CALL GetDailyAppointmentReportByDoctor('2026-08-03');



DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN input_month INT, 
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id, 
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        MONTH(appointment_time) = input_month 
        AND YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END $$

DELIMITER ;


<!--here's a sample call to test if the procedure is working. You may need to modify the date-->

CALL GetDoctorWithMostPatientsByMonth(9, 2026);


DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id, 
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END $$

DELIMITER ;

<!--here's a sample call to test if the procedure is working. You may need to modify the date-->
CALL GetDoctorWithMostPatientsByYear(2026);

# MongoDB seeding
As soon as springboot is running (mvn spring-boot:run) with no errors we'll connect to mongo shell (mongosh) using this command (if you change container name, this modification must be reflected below). Then, we'll enter our credentials.


sudo docker exec -it mongodb mongosh -u root -p


the required commands for the Document seeding are as follows (db will be created as data are inserted):
 
use prescriptions;

db.prescriptions.insertMany([
  {
  "_id": ObjectId("6807dd712725f013281e7201"),
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7202"),
  "patientName": "Emily Rose",
  "appointmentId": 52,
  "medication": "Aspirin",
  "dosage": "300mg",
  "doctorNotes": "Take 1 tablet after meals.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7203"),
  "patientName": "Michael Jordan",
  "appointmentId": 53,
  "medication": "Ibuprofen",
  "dosage": "400mg",
  "doctorNotes": "Take 1 tablet every 8 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7204"),
  "patientName": "Olivia Moon",
  "appointmentId": 54,
  "medication": "Antihistamine",
  "dosage": "10mg",
  "doctorNotes": "Take 1 tablet daily before bed.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7205"),
  "patientName": "Liam King",
  "appointmentId": 55,
  "medication": "Vitamin C",
  "dosage": "1000mg",
  "doctorNotes": "Take 1 tablet daily.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7206"),
  "patientName": "Sophia Lane",
  "appointmentId": 56,
  "medication": "Antibiotics",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 12 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7207"),
  "patientName": "Noah Brooks",
  "appointmentId": 57,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7208"),
  "patientName": "Ava Daniels",
  "appointmentId": 58,
  "medication": "Ibuprofen",
  "dosage": "200mg",
  "doctorNotes": "Take 1 tablet every 8 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7209"),
  "patientName": "William Harris",
  "appointmentId": 59,
  "medication": "Aspirin",
  "dosage": "300mg",
  "doctorNotes": "Take 1 tablet after meals.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7210"),
  "patientName": "Mia Green",
  "appointmentId": 60,
  "medication": "Vitamin D",
  "dosage": "1000 IU",
  "doctorNotes": "Take 1 tablet daily with food.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7211"),
  "patientName": "James Brown",
  "appointmentId": 61,
  "medication": "Antihistamine",
  "dosage": "10mg",
  "doctorNotes": "Take 1 tablet every morning.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7212"),
  "patientName": "Amelia Clark",
  "appointmentId": 62,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7213"),
  "patientName": "Ben Johnson",
  "appointmentId": 63,
  "medication": "Ibuprofen",
  "dosage": "400mg",
  "doctorNotes": "Take 1 tablet every 8 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7214"),
  "patientName": "Ella Monroe",
  "appointmentId": 64,
  "medication": "Vitamin C",
  "dosage": "1000mg",
  "doctorNotes": "Take 1 tablet daily.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7215"),
  "patientName": "Lucas Turner",
  "appointmentId": 65,
  "medication": "Aspirin",
  "dosage": "300mg",
  "doctorNotes": "Take 1 tablet after meals.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7216"),
  "patientName": "Grace Scott",
  "appointmentId": 66,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7217"),
  "patientName": "Ethan Hill",
  "appointmentId": 67,
  "medication": "Ibuprofen",
  "dosage": "400mg",
  "doctorNotes": "Take 1 tablet every 8 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7218"),
  "patientName": "Ruby Ward",
  "appointmentId": 68,
  "medication": "Vitamin D",
  "dosage": "1000 IU",
  "doctorNotes": "Take 1 tablet daily with food.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7219"),
  "patientName": "Jack Baker",
  "appointmentId": 69,
  "medication": "Antibiotics",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 12 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7220"),
  "patientName": "Mia Hall",
  "appointmentId": 70,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7221"),
  "patientName": "Owen Thomas",
  "appointmentId": 71,
  "medication": "Ibuprofen",
  "dosage": "200mg",
  "doctorNotes": "Take 1 tablet every 8 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7222"),
  "patientName": "Ivy Jackson",
  "appointmentId": 72,
  "medication": "Antihistamine",
  "dosage": "10mg",
  "doctorNotes": "Take 1 tablet every morning.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7223"),
  "patientName": "Leo Martin",
  "appointmentId": 73,
  "medication": "Vitamin C",
  "dosage": "1000mg",
  "doctorNotes": "Take 1 tablet daily.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7224"),
  "patientName": "Ella Moore",
  "appointmentId": 74,
  "medication": "Aspirin",
  "dosage": "300mg",
  "doctorNotes": "Take 1 tablet after meals.",
  "_class": "com.project.back_end.models.Prescription"
}
]);
