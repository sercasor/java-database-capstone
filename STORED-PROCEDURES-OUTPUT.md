# Output of CALL GetDailyAppointmentReportByDoctor('2025-04-15');

+-----------------+---------------------+--------+--------------+---------------+
| doctor_name     | appointment_time    | status | patient_name | patient_phone |
+-----------------+---------------------+--------+--------------+---------------+
| Dr. Ava Hall    | 2025-04-15 11:00:00 |      1 | Lucas Turner | 889-666-6666  |
| Dr. Mark Johnson| 2025-04-15 12:00:00 |      1 | Michael Jordan| 888-444-4444 |
| Dr. Mark Johnson| 2025-04-15 13:00:00 |      1 | Olivia Moon  | 888-555-5555  |
+-----------------+---------------------+--------+--------------+---------------+
3 rows in set (0.00 sec)

#Output of CALL GetDoctorWithMostPatientsByMonth(4, 2025);
+-----------+---------------+
| doctor_id | patients_seen |
+-----------+---------------+
|         2 |            30 |
+-----------+---------------+
1 row in set (0.00 sec)

#output of CALL GetDoctorWithMostPatientsByYear(2025);
+-----------+---------------+
| doctor_id | patients_seen |
+-----------+---------------+
|         1 |            33 |
+-----------+---------------+
1 row in set (0.00 sec)
