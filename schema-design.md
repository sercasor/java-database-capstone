## MySQL Database Design



### Table: doctors
- id: INT, Primary Key, Auto Increment
- userName: VARCHAR (30)
- passWord: VARCHAR (50)
- email: VARCHAR (100)
- phone: VARCHAR (20)
- name: VARCHAR (100)
- specialty: VARCHAR (50)
- availableTimes: VARCHAR (100)
<!--availability will be a private List<String> – List of available time slots (Example: "09:00 -10:00")-->



### Table: patients
- id: INT, Primary Key, Auto Increment
- userName: VARCHAR (30)
- passWord: VARCHAR (50)
- email: VARCHAR (100)
- phone: VARCHAR (20)
- name: VARCHAR (100)
- address: VARCHAR (100)


### Table: admins
- id: INT, Primary Key, Auto Increment
- userName: VARCHAR (30)
- passWord: VARCHAR (50)
- email: VARCHAR (100)


### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)
<!---->

## MongoDB Collection Design
<!--As of now, the only Document we can find will be presciptions-->
### Collection: prescriptions

<!-- DOCUMENT EXAMPLE
{
  "_id": ObjectId("64abc321456def7890123456"),
  "patient": {
    "patientId": 1,
    "name": "John Doe"
  },
  "doctor": {
    "doctorId": 1,
    "name": "Dr. Jane Smith"
  },
  "appointmentId": 38,
  "medication": {
    "name": "Ibuprofen",
    "dosage": "1g"
  },
  "instructions": "Take 1 tablet every 8 hours as needed for pain.",
  "refillCount": 2,
  "validUntil": ISODate("2025-12-31T23:59:59Z"),
  "datePrescribed": ISODate("2025-06-15T10:30:00Z"),
  "status": "Active"
}
-->
