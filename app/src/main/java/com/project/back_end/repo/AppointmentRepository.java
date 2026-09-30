package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment, Long> {

    //standard methods are implemented by Springboot

    /*-------------------------CUSTOM METHODS-------------------------*/
    //source: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    //2 joins are needed since joins  only work for 1 level. In this case, availableTimes is a @ElementCollection so theserecords belong to a additional table like doctor_available_times
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availableTimes WHERE d.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
    public List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    //CONCAT() is required since wildcards cannot be used alongside a named parameter due to the fact JPQL would interpret % as literal percentage symbols
    //the underscore delimits where the relation ends and the property (name) begins so it's less ambiguous. no "patientName" attribute is searched.
    // Instead, patient_name is translated to a.patient.name. Now SPring knows appointment has a Patient object property whose attribute is "name"
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html#repositories.query-methods.query-property-expressions
    @Query("SELECT a FROM Appointment a " +
            "LEFT JOIN FETCH a.doctor d " +
            "LEFT JOIN FETCH d.availableTimes " +
            "LEFT JOIN FETCH a.patient p" +
            " WHERE d.id = :doctorId " +
            "AND LOWER(p.name)" +
            "  LIKE LOWER(CONCAT('%', :patientName, '%')) " +
            " AND a.appointmentTime BETWEEN :start AND :end")
    public List<Appointment>  findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId")Long doctorId,
            @Param("patientName")String patientName,
            @Param("start")LocalDateTime start,
            @Param("end")LocalDateTime end
    );

    @Modifying //indicates this query modifies data (DML), unlike a SELECT instruction, which is DQL
    @Transactional //all operations are executed as a whole. Should an error occur for 1 of them, the whole process is reverted/aborted
    @Query("DELETE FROM Appointment a WHERE a.doctor.id = :doctorId") //JOIN FETCH unrequired since that instruction is aimed at storing some data in memory to be used. DELETE only needs an "address" to start removing records
    public void deleteAllByDoctorId(@Param("doctorId") Long doctorId);


    public List<Appointment> findByPatientId(Long patientId); //query annotation is not actually needed since no collection is fetched (thus not requiring a non-lazy or eager fetch strategy)

    public List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status); //query annotation is not actually needed. see above comment

    //mind the space after LOWER(CONCAT()), otherwise, JPQL joins the resultig expression with AND
    @Query("SELECT a FROM Appointment a " +
            "WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%',:doctorName,'%')) " +
            "AND a.patient.id = :patientId")
    public List<Appointment> filterByDoctorNameAndPatientId(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId);

    @Query("SELECT a FROM Appointment a " +
            "WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%',:doctorName,'%')) " +
            "AND a.patient.id = :patientId " +
            "AND a.status= :status")
    public List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId,
            @Param("status")int status
    );



}
